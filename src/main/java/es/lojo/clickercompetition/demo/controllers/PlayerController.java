package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.model.City;
import es.lojo.clickercompetition.demo.model.Player;
import es.lojo.clickercompetition.demo.repository.CityRepository;
import es.lojo.clickercompetition.demo.repository.PlayerRepository;
import es.lojo.clickercompetition.demo.services.ImageServices;
import es.lojo.clickercompetition.demo.services.PlayerUtilitiesService;
import es.lojo.clickercompetition.demo.utilities.StringManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private CityRepository citiRepo;

    @Autowired
    private PlayerUtilitiesService playerUtilitiesService;

    @Autowired
    private ImageServices imageServices;



    /**
     * Get all players
     * @return {ResponseEntity}: Json players
     */
    @GetMapping(value = "/players")
    public ResponseEntity<Object> allPlayersList(){
        return new ResponseEntity<>(playerRepo.findAll() , HttpStatus.OK);
    }

    /**
     * get all clicks sorted by clicks
     * @return {ResponseEntity}
     */
    @PostMapping(value = "/players/clicks")
    public ResponseEntity<Object> allPlayerListOrder(){
        return new ResponseEntity<>(playerRepo.allPlayersListOrder(), HttpStatus.OK);
    }

    /**
     * Add new player
     * It use capitalize by my own Library to save names
     * check if user exist
     * @param player {Player}: player to add
     * @return {ResponseEntity}
     */
    @PostMapping(value = "/player")
    public ResponseEntity<Object> playerAddSimple(@RequestBody Player player) throws IOException {
        if(playerUtilitiesService.addPlayerCheck(player))
            return new ResponseEntity<>(player.getName() +" player already exists ", HttpStatus.CONFLICT);

        playerRepo.save(player);
        return new ResponseEntity<>("Player with name " + player.getName() + " has been registered ", HttpStatus.OK );
    }

    /**
     * Add player with city
     * @param player {Player}: player to add
     * @param id {Long}: id of city to add
     * @return {ResponseEntity}
     * @throws IOException ResponseEntity NOT FOUND
     */
    @PostMapping(value = "/player/city/{id}")
    public ResponseEntity<Object> playerAddWithCity(@RequestBody Player player, @PathVariable("id") Long id) throws IOException {
        Optional<City> city = citiRepo.findById(id);

        if(city.isEmpty())
            return new ResponseEntity<>(" The city you are trying to associate does not exist ",HttpStatus.NOT_FOUND);

        if(playerUtilitiesService.addPlayerCheck(player))
            return new ResponseEntity<>(player.getName() +" player already exists ", HttpStatus.CONFLICT);

        player.setCity(city.get());
        playerRepo.save(player);
        return new ResponseEntity<>("Player with name " + player.getName() + " has been registered ", HttpStatus.OK );
    }

    /**
     * Delete an User by id
     * @param id {Long} delete an user by id
     * @return {ResponseEntity}
     */
    @DeleteMapping(value = "player/{id}")
    public ResponseEntity<Object> deletePlayer( @PathVariable("id") Long id){
        //Throw exception
        playerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        playerRepo.deleteById(id);
        return new ResponseEntity<>("Player with id "+id+" has been deleted", HttpStatus.OK);
    }

    /**
     * Update a player
     * @param player {player} player dates to update
     * @param id {id} id of the player to update
     * @return ResponseEntity
     */
    @PutMapping(value = "player/{id}")
    public ResponseEntity<Object> updatePlayer(@RequestBody Player player ,@PathVariable("id") Long id){
        playerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));

        player.setCapitalizedNames();
        player.setEncriptedPassword();
        playerRepo.save(player);
        return new ResponseEntity<>("Player with id "+id+ " has been updated", HttpStatus.OK);
    }

    /**
     * Set city
     * @param city {City}: city to add
     * @param id {Long}: id of user to update
     * @return ResponseEntity
     */
    @PutMapping(value = "player/city/{id}")
    public ResponseEntity<Object> updatePlayerCity(@RequestBody City city, @PathVariable("id") Long id){
        //Get Player
        //TODO: REFACTOR
        Optional<Player> optionalPlayer = playerRepo.findById(id);
        if(optionalPlayer.isEmpty())
            throw new EntityNotFoundException(id.toString());
        Player player = optionalPlayer.get();

        //Get City
        Optional<City> optionalCity = citiRepo.findCityByName(city.getName());
        if(optionalCity.isEmpty())
            throw new EntityNotFoundException(id.toString());
        city = optionalCity.get();

        //add city to player
        player.setCity(city);
        playerRepo.save(player);
        return new ResponseEntity<>(player.getName() + " updated with city " + city.getName(), HttpStatus.OK);
    }

    @GetMapping(value = "player/city/{id}")
    public ResponseEntity<Object> getPlayerCity(@PathVariable("id") Long id){
        Player player = playerRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
        if(player.getCity() != null){
            return new ResponseEntity<>(player.getCity(), HttpStatus.OK);
        }
        return new ResponseEntity<>(player.getName() + " has no assigned city ", HttpStatus.CONFLICT);
    }

    /**
     * Set and update player avatar
     * @param id {Long}: Player avatar id
     * @param file {file}: file to save
     * @return {ResponseEntity}
     */
    @PostMapping(value = "player/avatar")
    public ResponseEntity<Object> setPlayerAvatar(@RequestParam("id") Long id,
                                                  @RequestParam("file")MultipartFile file){

        //First check player id
        playerRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        try{
            imageServices.imageStore(file, id, "Player");
        }catch (Exception ex){
            return new ResponseEntity<>("something was wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("avatar of player with id " + id + " updated", HttpStatus.OK);
    }

    /**
     * Get player avatar
     * @param id {Long} : player id
     * @return {ResponseEntity}
     */
    @GetMapping(value = "player/avatar/{id}")
    public ResponseEntity<Object> getPlayerAvatar(@PathVariable("id") Long id){
        Player player = playerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        try {
            //Obtiene un objeto de tipo Path con la ubicación
            Path targetPath = Paths.get(player.getAvatar()).normalize();
            //Obtiene el recurso a partir de la ubicación
            Resource resource = new UrlResource(targetPath.toUri());
            if (resource.exists()) {
                //Ontiene el tipo de archivo
                String contentType = Files.probeContentType(targetPath);
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
            }else{
                throw new EntityNotFoundException(id.toString());
            }
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Delete a Player Avatar by id
     * @param id {Long} player id
     * @return {ResponseEntity}
     */
    @DeleteMapping(value = "player/avatar/{id}")
    public ResponseEntity<Object> deletePlayerAvatar(@PathVariable("id") Long id){
        Player player = playerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        imageServices.deleteImage(player.getAvatar());
        player.setAvatar("./images/default.png");
        playerRepo.save(player);
        return new ResponseEntity<>(player.getName() + "´s avatar deleted success", HttpStatus.OK);

    }
}

