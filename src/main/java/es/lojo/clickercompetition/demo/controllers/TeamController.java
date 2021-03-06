package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.model.Player;
import es.lojo.clickercompetition.demo.model.Team;
import es.lojo.clickercompetition.demo.repository.PlayerRepository;
import es.lojo.clickercompetition.demo.repository.TeamRepository;
import es.lojo.clickercompetition.demo.services.ImageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@RestController
public class TeamController {


    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private ImageServices imageServices;

    /**
     * list all teams
     * @return {ResponseEntity}
     */
    @GetMapping(value = "/teams")
    public ResponseEntity<Object> allTeamsList(){
        return new ResponseEntity<>(teamRepo.findAll(), HttpStatus.OK);
    }

    /**
     * List all teams with players
     * I have tried with the constructors to overwrite it and there was
     * no way that it would print what I need.
     * So play around with the lists to return equipment with users
     * @return {ResponseEntity}
     */
    @GetMapping(value = "/team/players")
    public ResponseEntity<Object> getAllTeamsWithPlayers(){
        ArrayList<Team> teams = (ArrayList<Team>) teamRepo.findAll();
        ArrayList<Object> result = new ArrayList<>();

        teams.forEach(team -> {
            ArrayList<Object> teamWithPlayer = new ArrayList<Object>();
            teamWithPlayer.add(team);
            teamWithPlayer.add(team.getPlayers());
            result.add(teamWithPlayer);
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Get player of a team by id
     * @param id {Long}: team id
     * @return {ResponseEntitya }
     */
    @GetMapping(value="team/players/{id}")
    public ResponseEntity<Object> getOneTeamWithPlayers(@PathVariable("id") Long id){
        Team team = teamRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        ArrayList<Object> teamWithPlayers = new ArrayList<Object>();
        teamWithPlayers.add(team);
        teamWithPlayers.add(team.getPlayers());
        return new ResponseEntity<>(teamWithPlayers, HttpStatus.OK);
    }

    /**
     * Get a team by id without players
     * @param id {Long}: Team id
     * @return ResponseEntity
     */
    @GetMapping(value = ("/team/{id}"))
    public ResponseEntity<Object> getOneTeam(@PathVariable("id") Long id){
        Team team = teamRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    /**
     * List all teams oredered by clicks
     * @return {ResponseEntity}
     */
    @GetMapping(value = "/teams/clicks")
    public ResponseEntity<Object> allTeamsListOrder(){
        return new ResponseEntity<>(teamRepo.findAllTeamssOrder(),HttpStatus.OK);
    }

    /**
     * Add new Team without Plauers
     * @param team
     * @return
     */
    @PostMapping(value ="/team")
    public ResponseEntity<Object> teamAddSimple(@RequestBody Team team){
        if(teamRepo.findTeamByName(team.getName()).isPresent())
            return new ResponseEntity<>("Team allready exists", HttpStatus.OK);
        team.setCapitalizedNames();
        team.setDefaulDate();
        team.setDefaultTeamAvatar();
        team.updateCliks();
        teamRepo.save(team);
        return new ResponseEntity<>("Team with name "+team.getName() + " has been registered", HttpStatus.OK);
    }

    /**
     * Add a player to team
     * @param id {Long} id of player to add
     * @param player
     * @return
     */
    @PostMapping(value = "/team/player/{id}")
    public ResponseEntity<Object> addPlayerToTeam(@PathVariable("id") Long id, @RequestBody Player player){
        Team team = teamRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));

        Player player1 = playerRepo.findPlayerByNameAndSurname(player.getName(), player.getSurname())
            .orElseThrow(()->new EntityNotFoundException(player.getName()));
        try{
            team.addPlayer(player1);
            team.updateCliks();
            teamRepo.save(team);
            return new ResponseEntity<>("Player has been associate with "+team.getName() + " team"
            ,HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>("Player is allready associate with another team or with this team",
                    HttpStatus.OK);
        }
    }

    /**
     * Delete a player of team
     * @param {Team} optionalTeam: Team ti delete
     * @param id {Long} player id o delete
     * @return {ResponseEntity}
     */
    @DeleteMapping(value="team/player/{id}")
    public ResponseEntity<Object> deletePlayerFromTeam(@RequestBody Team optionalTeam,@PathVariable("id") Long id){
        Team team = teamRepo.findTeamByName(optionalTeam.getName())
                .orElseThrow(()->new EntityNotFoundException(optionalTeam.getName()));
        Player player = playerRepo.findById(id).orElseThrow(()->new EntityNotFoundException(id.toString()));

        if(team.getPlayers().contains(player)){
            team.deleteOnePlayerFromTeam(player);
            team.updateCliks(); //when a player is deleted team must update clicks
            return new ResponseEntity<>(player.getName()+" with id "+id+" has been deleted\n" +
                    "from "+team.getName()+" team", HttpStatus.OK);
        }
        return new ResponseEntity<>("Player with id"+team.getName()+"isn`t associate with "+
                team.getName(), HttpStatus.OK);
    }

    /**
     * Delete a team
     * @param {Long} id: Team id to delete
     * @return {ResponseEnity}
     */
    @DeleteMapping(value="team/{id}")
    public ResponseEntity<Object> deleteTeam(@PathVariable("id") Long id){
        try{
            teamRepo.deleteById(id);
            return new ResponseEntity<>("Team with id "+id +" has been deleted", HttpStatus.OK);

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return new ResponseEntity<>("Please first delete players of team", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Update a team
     * @param team {Team}: team with data to update
     * @param id {Long}: id of the team to update
     * @return {ResponseEntity}
     */
    @PutMapping(value= "team/{id}")
    public ResponseEntity<Object> updateTeam(@RequestBody Team team, @PathVariable("id") Long id){
        Team oldTeam = teamRepo.findById(id).orElseThrow(
                ()->new EntityNotFoundException(id.toString()));
        team.setCapitalizedNames();
        //Update name
        oldTeam.setName(team.getName());
        if(!team.getPlayers().isEmpty()){
            try{
                //Update team playersa
                oldTeam.setPlayers(team.getPlayers());
                oldTeam.updateCliks();
            }catch (Exception ex){
                return new ResponseEntity<>("Player allready at this or another team, " +
                        "First remove form his team", HttpStatus.CONFLICT);
            }
        }
        teamRepo.save(oldTeam);
        return new ResponseEntity<>(team.getName()+" has been updated", HttpStatus.OK);
    }


    /**
     * Set amd update player avatar
     * @param id {Long}: id of team to update
     * @param file {MultipartFile}: image to save
     * @return {ResponseEntity}
     */
    @PostMapping(value = "team/avatar")
    public ResponseEntity<Object> setTeamAvatar(@RequestParam("id") Long id,
                                                @RequestParam("file")MultipartFile file){
        //First check player id
        teamRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        try{
            imageServices.imageStore(file, id, "Team");
        }catch (Exception ex){
            return new ResponseEntity<>("something was wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("avatar of player with id " + id + " updated", HttpStatus.OK);
    }

    /**
     * get team avatar
     * @param id {Long}: id of team to get
     * @return {ResponseEntity}
     */
    @GetMapping(value= "team/avatar/{id}")
    public ResponseEntity<Object> getTeamAvatar(@PathVariable("id") Long id){
        Team team = teamRepo.findById(id).orElseThrow(()->new EntityNotFoundException(id.toString()));
        try{
            Path targetPath = Paths.get(team.getTeamAvatar()).normalize();
            Resource resource = new UrlResource(targetPath.toUri());
            if(resource.exists()){
                String contentType = Files.probeContentType(targetPath);
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            }else{
                throw new EntityNotFoundException(id.toString());
            }
        }catch (IOException ex){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }


    /**
     * Delete a team avatar
     */
    @DeleteMapping(value="team/avatar/{id}")
    public ResponseEntity<Object> deleteTeamAvatar(@PathVariable("id") Long id){
        Team team = teamRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        if(team.getTeamAvatar().equals("./images/default.png")){
            return new ResponseEntity<>(team.getName() + "' doesn´t have avatar", HttpStatus.OK);
        }
        imageServices.deleteImage(team.getTeamAvatar());
        team.setTeamAvatar("./images/default.png");
        teamRepo.save(team);
        return new ResponseEntity<>(team.getName() + "´s avatar deleted success", HttpStatus.OK);
    }
}


