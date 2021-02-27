package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.model.AuthonomusCommunity;
import es.lojo.clickercompetition.demo.model.City;
import es.lojo.clickercompetition.demo.repository.AuthonomusCommunityRepository;
import es.lojo.clickercompetition.demo.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
public class CityController {

    @Autowired
    private CityRepository cityRepo;

    @Autowired
    private AuthonomusCommunityRepository authonomusCommunityRep;

    /**
     * List all Cities
     * @return {ResponseEntity}: List of Cities
     */
    @GetMapping(value = "/cities")
    public ResponseEntity<Object> allCitiesList(){
        return new ResponseEntity<>(cityRepo.findAll(), HttpStatus.OK);
    }

    /**
     * List all Cities Order by count clicks
     * @return {ResponseEntity}: Ordered
     */
    @PostMapping(value = "/cities/clicks")
    public ResponseEntity<Object> allCityListOrder(){
        return new ResponseEntity<>(cityRepo.cityListAllOrder(), HttpStatus.OK);
    }

    /**
     * Get one city
     * @param id {Long}: City id
     * @return {ResponseEntity}
     */
    @GetMapping(value = "/city/{id}")
    public ResponseEntity<Object> getOnCity(@PathVariable("id") Long id){
        City city = cityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        return new ResponseEntity<>(city,HttpStatus.OK);
    }

    /**
     * Add new City without AuthonomusCommunity
     * @param city {City} : city to add
     * @return {ResponseEntity}
     */
    @PostMapping(value = "/city")
    public ResponseEntity<Object> cityAddSimple(@RequestBody City city){
        if(cityRepo.findCityByName(city.getName()).isPresent())
            return new ResponseEntity<>("City allready exists", HttpStatus.CONFLICT);
        city.setCapitalizedName();
        cityRepo.save(city);
        return new ResponseEntity<>("City with name " + city.getName() + " has been registered",HttpStatus.OK);
    }

    /**
     * Add new City with AutonomousCommunity
     * @param city {City}: City to add
     * @param id {Long}: AuthonomousCommunity to associate
     * @return {ResponseEntity}
     */
    @PostMapping(value = "/city/{id}")
    public ResponseEntity<Object> cityAddWithAc(@RequestBody City city, @PathVariable("id") Long id){
        Optional<AuthonomusCommunity> authonomusCommunityOptinal = authonomusCommunityRep.findById(id);
        if(authonomusCommunityOptinal.isEmpty())
            return new ResponseEntity<>("The Authonomus Community you are trying to associate does not exists ", HttpStatus.NOT_FOUND);

        if(cityRepo.findCityByName(city.getName()).isPresent())
            return new ResponseEntity<>("city allready exists", HttpStatus.CONFLICT);
        city.setCapitalizedName();
        city.setAuthonomusCommunity(authonomusCommunityOptinal.get());
        cityRepo.save(city);
        return new ResponseEntity<>("City with name " + city.getName() + " has been registered",HttpStatus.OK);
    }

    /**
     * Delete a city
     * @param id {Long}: id of city to delete
     * @return {ResponseEntity}
     */
    @DeleteMapping(value = "/city/{id}")
    public ResponseEntity<Object> deleteCity(@PathVariable("id") Long id){

        City city = cityRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        //check that there are no associated players
        try{
            cityRepo.delete(city);
            return new ResponseEntity<>("City with id "+id+" has been deleted",HttpStatus.OK);
        }catch(Exception ex){
            //There are users associates to this city
            return new ResponseEntity<>(city.getName() + " already has associated players ",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a city
     * @param city {City}: city to update
     * @param id {Long}: city id to update
     * @return {ResponseEntity}
     */
    @PutMapping(value = "city/{id}")
    public ResponseEntity<Object> updateCity(@RequestBody City city, @PathVariable("id") Long id){
        City oldCity = cityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        oldCity.setName(city.getName());
        oldCity.setCapitalizedName();
        cityRepo.save(oldCity);
        return new ResponseEntity<>("City with id "+id+" was has been updated", HttpStatus.OK);
    }

    /**
     * TODO: FIJARME EN updateCity
     * because I just want to update the foreign one
     * @param authonomusCommunity {AuthonomusCommunity}: Autonomous Community to add
     * @param id {Long}:City to update id
     * @return {ResponseEntity}
     */
    @PutMapping(value = "city/AutonomousCommunity/{id}")
    public ResponseEntity<Object> updateCityAc(@RequestBody AuthonomusCommunity authonomusCommunity, @PathVariable("id") Long id){
        //Check City
        City city = cityRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
        //Check Autonomous Community
        Optional<AuthonomusCommunity> optionalAuthonomusCommunity = authonomusCommunityRep
                .findAuthonomusCommunityByName(authonomusCommunity.getName());
        if(optionalAuthonomusCommunity.isEmpty())
            return new ResponseEntity<>(authonomusCommunity.getName() + "does not ecists", HttpStatus.NOT_FOUND);

        city.setAuthonomusCommunity(optionalAuthonomusCommunity.get());
        cityRepo.save(city);
        return new ResponseEntity<>(city.getName() + " update with Autonomous Community " +
                authonomusCommunity.getName(),HttpStatus.OK);
    }
    //TODO: GET AUTHONOMOUS COMMUNITY FROM CITY
    //TODO: TEST CONTROLLERS
}
