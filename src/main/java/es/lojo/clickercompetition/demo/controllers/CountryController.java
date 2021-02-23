package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {


    @Autowired
    private CountryRepository countryRepo;

    //Get All country
    @GetMapping(value = "/countries")
    public ResponseEntity<Object> allCountryList(){
        return new ResponseEntity<>(countryRepo.findAll(), HttpStatus.OK);
    }

    //GET Countries ordered
    @PostMapping(value = "/countries")
    public ResponseEntity<Object> allCountryListOrder(){
        return new ResponseEntity<>(countryRepo.getOrderByClicksCountry(), HttpStatus.OK);
    }
}
