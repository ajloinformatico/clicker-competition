package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.model.City;
import es.lojo.clickercompetition.demo.model.Country;
import es.lojo.clickercompetition.demo.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
public class CountryController {

    //TODO TEST

    @Autowired
    CountryRepository countryRepository;


    @Autowired
    private CountryRepository countryRepo;

    /**
     * Return all countries
     * @return {ResponseEntity}
     */
    @GetMapping(value = "/countries")
    public ResponseEntity<Object> allCountryList(){
        return new ResponseEntity<>(countryRepo.findAll(), HttpStatus.OK);
    }

    /**
     * Return all countries ordered
     * @return {ResponseEntity}
     */
    @GetMapping(value = "/countries/clicks")
    public ResponseEntity<Object> allCountryListOrder(){
        return new ResponseEntity<>(countryRepo.getOrderByClicksCountry(), HttpStatus.OK);
    }

    /**
     * Get one country
     * @param id {Long}: id of the country to get
     * @return {ResponseEntity}
     */
    @GetMapping(value = "/country/{id}")
    public ResponseEntity<Object> getOneCountry(@PathVariable("id") Long id){
        Country country = countryRepo.findById(id).orElseThrow(()->new EntityNotFoundException(id.toString()));
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    /**
     * Add a new country
     * @param country {Country}: country data to save
     * @return {ResponseEntity}
     */
    @PostMapping(value = "/country")
    public ResponseEntity<Object> addSimpleCountry(@RequestBody Country country){
        country.setCapitalizedName();
        if(countryRepo.findCountryByName(country.getName()).isPresent())
            return new ResponseEntity<>("Country already exists", HttpStatus.CONFLICT);
        countryRepo.save(country);
        return new ResponseEntity<>("Country with name "+country.getName(),HttpStatus.OK);
    }

    /**
     * Delete a country by id
     * @param id {Long}: Country id
     * @return {ResponseEntity}
     */
    @DeleteMapping(value = "/country/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable("id") Long id){
        Country country = countryRepo.findById(id).orElseThrow(()->new EntityNotFoundException(id.toString()));
        try{
            countryRepo.delete(country);
            return new ResponseEntity<>(country.getName() + " has been deleted ", HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>("Something was wrong ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an autonomous community
     * @param country {Country}: country to update
     * @param id {Long}: id to update
     * @return {ResponsEntity}
     */
    @PutMapping(value = "/country/{id}")
    public ResponseEntity<Object> updateCountry(@RequestBody Country country,
                                                @PathVariable("id") Long id){
        Country oldCountry = countryRepo.findById(id).orElseThrow(()->new EntityNotFoundException(id.toString()));
        country.setCapitalizedName();
        if(countryRepo.findCountryByName(country.getName()).isPresent())
            return new ResponseEntity<>("This name is already associate with another country ",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        oldCountry.setName(country.getName());
        countryRepo.save(country);
        return new ResponseEntity<>("Country with id "+id+" has been updated", HttpStatus.OK);
    }

}
