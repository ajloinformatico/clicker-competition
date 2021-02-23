package es.lojo.clickercompetition.demo.services;


import es.lojo.clickercompetition.demo.model.Player;
import es.lojo.clickercompetition.demo.model.Team;
import es.lojo.clickercompetition.demo.repository.PlayerRepository;
import es.lojo.clickercompetition.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.*;

/**
 * @author antoniojoselojoojeda
 */
 @Service
public class ImageServices {
    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private TeamRepository teamRepo;

    /**
     * Save an image to "localStorage" and update database
     * @param file {MultipartFile}: file to save
     * @param id {Long}: File to save
     * @param type {String}: type of Entity
     */
    public void imageStore(MultipartFile file, Long id, String type) throws IOException {
        //Catch extension
        String extension = "";
        try {
            String original = file.getOriginalFilename();
            assert original != null;
            extension = original.substring(original.lastIndexOf("."));
        } catch (Exception ex) {
            new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //Storage image
        String myFileName = type + id.toString() + extension;

        Path targetPath = Paths.get("./images/" + myFileName).normalize();

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        String urlSave = "./images/" + myFileName;

        //Check type and add url to Database
        if (type.equals("Player")) {
            Player player = playerRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(id.toString()));
            player.setAvatar(urlSave);
            playerRepo.save(player);
        } else {
            Team team = teamRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(id.toString()));
            //TODO Avatar
        }
    }

        /**
         * Delete an image, only with the url because it
         * already incorporates type and id Even so,
         * each entity has a unique id and each image has its own url
         * @param imageUrl {String}: url image to delete
         */
    public void deleteImage(String imageUrl){
        try {
            Files.deleteIfExists(Paths.get(imageUrl));
        } catch(NoSuchFileException | DirectoryNotEmptyException e) {
           throw new EntityNotFoundException("Image doesnt exists");
        } catch(IOException e) {
            new ResponseEntity<>("You don't have permissions", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }



 }



