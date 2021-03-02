package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.model.Country;
import es.lojo.clickercompetition.demo.repository.AuthonomusCommunityRepository;
import es.lojo.clickercompetition.demo.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.lojo.clickercompetition.demo.model.AuthonomusCommunity;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
public class CommunityController {


    @Autowired
    AuthonomusCommunityRepository communityRepository;

    @Autowired
    CountryRepository countryRepository;


    /**
     * List all Communities
     * @return {ResponseEntity}: Communities Json
     */
    @GetMapping(value = "/communities")
    public ResponseEntity<Object> allCommunitieslist(){
        return new ResponseEntity<>(communityRepository.findAll(), HttpStatus.OK);
    }

    /**
     * List all Communities ordered
     * @return {ResponseEntity}: Communities Json Ordered
     */
    @GetMapping(value= "/communities/clicks")
    public ResponseEntity<Object> allCommunitiesListOrder(){
        return new ResponseEntity<>(communityRepository.allComunitiesListOrder(), HttpStatus.OK);
    }

    /**
     * Return one community
     * @param id {Long}: Autonomous Community id
     * @return {Response Entity}
     */
    @GetMapping(value = "community/{id}")
    public ResponseEntity<Object> getOneCommunity(@PathVariable("id") Long id){
        AuthonomusCommunity ac = communityRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        return new ResponseEntity<>(ac, HttpStatus.OK);
    }

    /**
     * Add new AutonomousCommunity
     * @param ac {AutonomusCommunity} : add new AutonomousCommunity
     * @return {ResponseEntity}
     */
    @PostMapping(value = "/community")
    public ResponseEntity<Object> communityAddSimple(@RequestBody AuthonomusCommunity ac){
        ac.setCapitalizedName();
        if(communityRepository.findAuthonomusCommunityByName(ac.getName()).isPresent())
            return new ResponseEntity<>("Autonomous Community already exists", HttpStatus.CONFLICT);
        communityRepository.save(ac);
        return new ResponseEntity<>("Autonomous Community with name "+ac.getName(),HttpStatus.OK);
    }

    /**
     * Add new Autonomous Community with a Country
     * @param ac {Autonomous Community} : Autonomous Community to add
     * @param id {Long}: id of a Country to add
     * @return {ResponseEntity}
     */
    @PostMapping(value = "community/{id}")
    public ResponseEntity<Object> communityAddWithCountry(@RequestBody AuthonomusCommunity ac,
                                                          @PathVariable("id") Long id){
        Country country = countryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        ac.setCapitalizedName();
        if(communityRepository.findAuthonomusCommunityByName(ac.getName()).isPresent()){
            return new ResponseEntity<>("Autonomous Community already exists", HttpStatus.CONFLICT);
        }
        ac.setCountry(country);
        communityRepository.save(ac);
        return new ResponseEntity<>("Autonomous Community with name "+ac.getName() + 
                "has been registered", HttpStatus.OK);
    }

    /**
     * Delete a Autonomous Community by an id
     * @param id {Long} : Autonomous Community id
     * @return {ResponseEntity}
     */
    @DeleteMapping(value = "community/{id}")
    public ResponseEntity<Object> deleteAutonomousCommunity(@PathVariable("id") Long id){
        AuthonomusCommunity ac = communityRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        try{
            communityRepository.delete(ac);
            return new ResponseEntity<>("Autonomous Community with id "+id+" has been deleted", HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(ac.getName() + " already has associated one City", HttpStatus.CONFLICT);
        }
    }

    /**
     * Update an autonomousCommunity
     * @param id {Long} AutonomousCommunity to update
     * @param ac {Autonomous Community} AutonomousCommunity data to save
     * @return {ResponseEntity}
     */
    @PutMapping(value="community/{id}")
    public ResponseEntity<Object> updateCommunity(@PathVariable("id") Long id,
                                                  @RequestBody AuthonomusCommunity ac){
        AuthonomusCommunity oldAc = communityRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        ac.setCapitalizedName();
        if(communityRepository.findAuthonomusCommunityByName(ac.getName()).isPresent())
            return new ResponseEntity<>("This name is already associate with another Autonomous Community",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        oldAc.setName(ac.getName());
        communityRepository.save(oldAc);
        return new ResponseEntity<>("Autonomus Community with id "+id+" has been update", HttpStatus.OK);
    }


    /**
     * Update the country of a Autonomous Community by an id
     * @param id {Long}: Autonomous Community id to update
     * @param country {Country}: Country data to update
     * @return {ResponseEntitu}
     */
    @PutMapping(value = "community/country/{id}")
    public ResponseEntity<Object> updateCommunityCountry(@PathVariable("id") Long id,
                                                            @RequestBody Country country){
        AuthonomusCommunity ac = communityRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        Country country1 = countryRepository.findCountryByName(country.getName())
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        ac.setCountry(country1);
        communityRepository.save(ac);
        return new ResponseEntity<>(ac.getName() + " updated with Country "
                + country1.getName(), HttpStatus.OK);
    }

    /**
     * Get country of a autonomous Community
     * @param id {Long}
     * @return
     */
    @GetMapping(value = "community/country/{id}")
    public ResponseEntity<Object> getAutonomousCommunityCountry(@PathVariable("id") Long id){
        AuthonomusCommunity ac = communityRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        if(ac.getCountry() == null)
            return new ResponseEntity<>(ac.getName() + " has not country associated",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ac.getCountry(), HttpStatus.OK);
    }

    /**
     * Break the relationship between autonomous Community and country
     * @param id {Long}: City id
     * @return {ResponseEntity}
     */
    @DeleteMapping(value = "community/country/{id}")
    public ResponseEntity<Object> deleteCityAuc(@PathVariable("id") Long id){
        AuthonomusCommunity ac = communityRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));

        if(ac.getCountry() == null)
            return new ResponseEntity<>(ac.getName()+" has not country associated", HttpStatus.NOT_FOUND);
        ac.setCountry(null);
        communityRepository.save(ac);
        return new ResponseEntity<>(ac.getName()+" no longer has country", HttpStatus.OK);
    }
}
