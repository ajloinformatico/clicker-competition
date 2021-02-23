package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.repository.AuthonomusCommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunityController {


    @Autowired
    AuthonomusCommunityRepository communityRepository;


    /**
     * List all Communities
     * @return {ResponseEntity}: Communities Json
     */
    @GetMapping(value = "/comunities")
    public ResponseEntity<Object> allCommunitieslist(){
        return new ResponseEntity<>(communityRepository.findAll(), HttpStatus.OK);
    }

    /**
     * List all Communities ordered
     * @return {ResponseEntity}: Communities Json Ordered
     */
    @PostMapping(value= "/comunities")
    public ResponseEntity<Object> allCommunitiesListOrder(){
        return new ResponseEntity<>(communityRepository.allComunitiesListOrder(), HttpStatus.OK);
    }
}
