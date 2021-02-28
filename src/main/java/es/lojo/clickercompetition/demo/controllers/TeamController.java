package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.model.Player;
import es.lojo.clickercompetition.demo.model.Team;
import es.lojo.clickercompetition.demo.repository.PlayerRepository;
import es.lojo.clickercompetition.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

@RestController
public class TeamController {


    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private PlayerRepository playerRepo;

    //TODO: TESTS

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
        team.updateCliks();
        teamRepo.save(team);
        return new ResponseEntity<>("Team with name "+team.getName() + " has been registered", HttpStatus.OK);
    }

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
            return new ResponseEntity<>(player.getName()+" with id "+id+" has been deleted\n" +
                    "from "+team.getName()+" team", HttpStatus.OK);
        }
        return new ResponseEntity<>("Player with id"+team.getName()+"isn`t associate with "+
                team.getName(), HttpStatus.OK);
    }

    /**
     * TODO: TEST DELETE & UPDATE TEAM
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
     *
     * It's only when it is created
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
            }catch (Exception ex){
                return new ResponseEntity<>("Player u allready at this or another team, " +
                        "First remove form his team", HttpStatus.CONFLICT);
            }
        }
        teamRepo.save(oldTeam);
        return new ResponseEntity<>(team.getName()+" has been updated", HttpStatus.OK);



    }
}


