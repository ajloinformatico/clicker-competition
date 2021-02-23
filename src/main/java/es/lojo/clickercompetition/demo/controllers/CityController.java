package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.model.AuthonomusCommunity;
import es.lojo.clickercompetition.demo.model.City;
import es.lojo.clickercompetition.demo.repository.AuthonomusCommunityRepository;
import es.lojo.clickercompetition.demo.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    @PostMapping (value = "/cities/clicks")
    public ResponseEntity<Object> allCityListOrder(){
        return new ResponseEntity<>(cityRepo.cityListAllOrder(), HttpStatus.OK);
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

        city.setAuthonomusCommunity(authonomusCommunityOptinal.get());
        cityRepo.save(city);
        return new ResponseEntity<>("City with name " + city.getName() + " has been registered",HttpStatus.OK);

    }






}
