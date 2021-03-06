package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.model.City;
import es.lojo.clickercompetition.demo.model.Player;
import es.lojo.clickercompetition.demo.model.Role;
import es.lojo.clickercompetition.demo.repository.CityRepository;
import es.lojo.clickercompetition.demo.repository.PlayerRepository;
import es.lojo.clickercompetition.demo.repository.RoleRepository;
import es.lojo.clickercompetition.demo.repository.TeamRepository;
import es.lojo.clickercompetition.demo.services.ImageServices;
import es.lojo.clickercompetition.demo.services.PlayerUtilitiesService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@RestController
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private CityRepository citiRepo;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerUtilitiesService playerUtilitiesService;

    @Autowired
    private ImageServices imageServices;

    @Autowired
    private RoleRepository roleRepository;

    /**
     *
     * @param id {Long}: Player id
     * @return {ResponseEntity}
     */
    @GetMapping("/rol/{id}")
    public ResponseEntity<Object> roles(@PathVariable("id") Long id) {
        Player player = playerRepo.findById(id).orElseThrow(()->new EntityNotFoundException(id.toString()));
        return new ResponseEntity<>(player.getRole(), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout() {
        Player player = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        player.setToken(null);
        playerRepo.save(player);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam("name") String name,
                                        @RequestParam("password") String password) {
        String token = null;
        // Test user/password
        Player player = playerRepo.findPlayerByName(name)
                .orElseThrow(() -> new EntityNotFoundException(name));
        if(!new BCryptPasswordEncoder().matches(password, player.getPassword()))
//            return new ResponseEntity<>("Incorrect password", HttpStatus.FORBIDDEN);
            throw new EntityNotFoundException();
        // Compruebo que el usuario tenga token generado y no esté caducado...
        if(player.getToken() != null) {
            try {
                Jwts.parser().parse(player.getToken()).getBody();
                return new ResponseEntity<>("", HttpStatus.CONFLICT);
            } catch (Exception e) {
                player.setToken(null);
            }
        }
        // Generate token
//        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String secretKey = "pestillo";
        // TODO: Investigar todos los parámetros
        token = Jwts
                .builder()
                .setId("AlbertIES")
                .setSubject(name)
                .claim("authorities",
                        player.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 6000000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
        player.setToken(token);
        playerRepo.save(player);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }




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
    @GetMapping(value = "/players/clicks")
    public ResponseEntity<Object> allPlayerListOrder(){
        return new ResponseEntity<>(playerRepo.allPlayersListOrder(), HttpStatus.OK);
    }

    /**
     * Return a player
     * @param id {Long}: player id
     * @return {ResponseEntity}
     */
    @GetMapping(value = "/player/{id}")
    public ResponseEntity<Object> getOnePlayer(@PathVariable("id") Long id){
        Optional<Player> player = playerRepo.findById(id);
        if(player.isEmpty())
            return new ResponseEntity<>("no found",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(player.get(), HttpStatus.OK);
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

        //Set correct role
        Role role = roleRepository.findRoleByName(player.getRole().getName())
                .orElseThrow(() -> new EntityNotFoundException(player.getRole().getName()));
        player.setRole(role);

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

        //Set correct role
        Role role = roleRepository.findRoleByName(player.getRole().getName())
                .orElseThrow(() -> new EntityNotFoundException(player.getRole().getName()));
        player.setRole(role);

        //Check city
        if(city.isEmpty())
            return new ResponseEntity<>(" The city you are trying to associate does not exist ",HttpStatus.NOT_FOUND);

        //ajusta player
        if(playerUtilitiesService.addPlayerCheck(player))
            return new ResponseEntity<>(player.getName() +" player already exists ", HttpStatus.CONFLICT);


        player.setCity(city.get());
        player.setRole(role);
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
        Player player = playerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        try{
            playerRepo.delete(player);
            return new ResponseEntity<>("Player with id "+id+" has been deleted", HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>("Player is already associate with a team\nRemove first from team",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a player with this method the image is also updated
     * but I decided to separate it into different requests
     * @param player {player} player dates to update
     * @param id {id} id of the player to update
     * @return ResponseEntity
     */
    @PutMapping(value = "player/{id}")
    public ResponseEntity<Object> updatePlayer(@RequestBody Player player ,@PathVariable("id") Long id){
        Player oldPlayer = playerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        player.setCapitalizedNames();
        if(playerRepo.findPlayerByName(player.getName()).isPresent())
            return new ResponseEntity<>("This name is already associated with another player"
                    ,HttpStatus.INTERNAL_SERVER_ERROR);
        //Set id
        player.setId(oldPlayer.getId());
        player.setEncriptedPassword();
        //Set role because user data comes without role
        player.setRole(oldPlayer.getRole());
        playerRepo.save(player);
        return new ResponseEntity<>("Player with id "+id+ " has been updated", HttpStatus.OK);
    }

    /**
     * Update clicks from front each 30 cliks
     */
    @PutMapping(value = "player/clicks/{id}")
    public ResponseEntity<Object> UpdateClicks(@PathVariable("id") Long id) throws IOException{
        Player player = playerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        player.setClicks(player.getClicks() + 30);
        playerRepo.save(player);
        return new ResponseEntity<>(id+" - "+player.getClicks(), HttpStatus.OK);
    }


    /**
     * @param city {City}: city to add
     * @param id {Long}: id of user to update
     * @return ResponseEntity
     */
    @PutMapping(value = "player/city/{id}")
    public ResponseEntity<Object> updatePlayerCity(@RequestBody City city, @PathVariable("id") Long id){
        //Get Player
        Optional<Player> optionalPlayer = playerRepo.findById(id);
        if(optionalPlayer.isEmpty())
            throw new EntityNotFoundException(id.toString());
        Player player = optionalPlayer.get();

        //Get City
        Optional<City> optionalCity = citiRepo.findCityByName(city.getName());
        if(optionalCity.isEmpty())
            return new ResponseEntity<>(city.getName() + " does not exists ",HttpStatus.NOT_FOUND);
        city = optionalCity.get();

        //add city to player
        player.setCity(city);
        playerRepo.save(player);
        return new ResponseEntity<>(player.getName() + " updated with city " + city.getName(), HttpStatus.OK);
    }

    /**
     * Get a city of a player
     * @param id {Long}: id player to get
     * @return {ResponseEntity}
     */
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
        if(player.getAvatar().equals("./images/default.png")){
            return new ResponseEntity<>(player.getName() + "' doesn´t have avatar", HttpStatus.OK);
        }
        imageServices.deleteImage(player.getAvatar());
        player.setAvatar("./images/default.png");
        playerRepo.save(player);
        return new ResponseEntity<>(player.getName() + "´s avatar deleted success", HttpStatus.OK);
    }


}

