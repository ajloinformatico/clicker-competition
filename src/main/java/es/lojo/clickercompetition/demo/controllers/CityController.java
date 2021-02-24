package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.model.AuthonomusCommunity;
import es.lojo.clickercompetition.demo.model.City;
import es.lojo.clickercompetition.demo.model.Player;
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
        Optional<City> optionalCity = cityRepo.findById(id);
        if(optionalCity.isEmpty())
            return new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(optionalCity.get(),HttpStatus.OK);
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
        cityRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException(id.toString()));
        cityRepo.deleteById(id);
        return new ResponseEntity<>("City with id "+id+" has been deleted",HttpStatus.OK);
    }

    /**
     * Delete a city
     * @param city {City}: city to update
     * @param id {Long}: city id to update
     * @return {ResponseEntity}
     */
    @PutMapping(value = "city/{id}")
    public ResponseEntity<Object> updateCity(@RequestBody City city, @PathVariable("id") Long id){
        cityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        city.setCapitalizedName();
        cityRepo.save(city);
        return new ResponseEntity<>("City with id "+id+" was has been updated", HttpStatus.OK);
    }

    /**
     * Only update Authonomous Community of a city
     * @param city {City}: City to update
     * @param id {Long}: AutonomousCommunity id
     * @return {ResponseEntity}
     */
    @PutMapping(value = "city/AutonomousCommunity/{id}")
    public ResponseEntity<Object> updateCityAc(@RequestBody City city, @PathVariable("id") Long id){
        //Check City
        Optional<City> optionalCity = cityRepo.findById(city.getId());
        if(optionalCity.isEmpty())
            throw new EntityNotFoundException(id.toString());

        //Check AC
        Optional<AuthonomusCommunity> optionalAuthonomusCommunity = authonomusCommunityRep.findById(id);
        if(optionalAuthonomusCommunity.isEmpty())
            throw new EntityNotFoundException(id.toString());
        city.setAuthonomusCommunity(optionalAuthonomusCommunity.get());
        return new ResponseEntity<>(city.getName() + "update wit AC "+
                optionalAuthonomusCommunity.get().getName(), HttpStatus.OK);
    }
    //TODO: GET AUTHONOMOUS COMMUNITY FROM CITY
    //TODO: TEST CONTROLLERS
}
