package es.lojo.clickercompetition.demo;

import static org.assertj.core.api.Assertions.assertThat;

import es.lojo.clickercompetition.demo.controllers.*;
import es.lojo.clickercompetition.demo.model.*;
import es.lojo.clickercompetition.demo.repository.*;
// import org.assertj.core.api.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
class ApplicationTests {

//    @BeforeAll //STATIC it is called before any test
//
//    @BeforeEach //Prepare the tests only one
//
//    @AfterAll //STATIC it is called after all tests
//
//    @AfterEach //Its called after one test
//
//    Arrange --> prepare
//
//    add --> probe
//
//    assert --> check

    //repositories
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private AuthonomusCommunityRepository communityRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    //controllers
    @Autowired
    private CountryController countryController;

    @Autowired
    private CommunityController communityController;

    @Autowired
    private CityController cityController;

    @Autowired
    private PlayerController playerController;

    @Autowired
    public TeamController teamController;


    //TEST Country

    /**
     * Check Country Controller context
     */
    @Test
    public void CountryControllerContexLoad() {
        assertThat(countryController).isNotNull();
    }

    /**
     * Check testAllCountryList
     */
    @Test
    public void testAllCountryLis() {
        // Check HttpCode
        ResponseEntity<Object> httpResponse = countryController.allCountryList();
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response body is correct
        Assertions.assertEquals(httpResponse.getBody(), countryRepository.findAll());
    }

    /**
     * Check allCountryListOrder
     */
    @Test
    public void testAllCountryListOrder(){
        //Check HttpCode
        ResponseEntity<Object> httpResponse = countryController.allCountryListOrder();
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response body is correct
        Assertions.assertEquals(httpResponse.getBody(), countryRepository.getOrderByClicksCountry());
    }

    /**
     * Check getOneCountry
     */
    @Test
    public void testGetOneCountry(){
        //Check HttpCode
        ResponseEntity<Object> httpResponse = countryController.getOneCountry(1L);
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response body is correct
        Optional<Country> country = countryRepository.findById(1L);
        // Check repo find country
        assertThat(country).isPresent();
        // Check response with country
        Assertions.assertEquals(httpResponse.getBody(), country.get());
    }


    /**
     * Check add Country
     */
    @Test
    public void testAddSimpleCountry(){
        Country country = new Country("Islandia");
        ResponseEntity<Object> httpResponse = countryController.addSimpleCountry(country);
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response with country
        Assertions.assertEquals(httpResponse.getBody(), "Country with name "+country.getName());
    }

    /**
     * Check delete country
     */
    @Test
    public void testDeleteCountry(){
        Country country =  countryRepository.save(new Country("Isandia"));
        ResponseEntity<Object> httpResponse = countryController.deleteCountry(country.getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        //Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response with country
        Assertions.assertEquals(httpResponse.getBody(), country.getName()+" has been deleted ");
    }

    /**
     * Check update country
     */
    @Test
    public void updateCountry(){
        Country oldCountry = countryRepository.save(new Country("Islandia"));
        Country newCountry = new Country("infolojosCountry");
        ResponseEntity<Object> httpResponse = countryController.updateCountry(newCountry, oldCountry.getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        //Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        //Check data
        assert countryRepository.findCountryByName(newCountry.getName()).isPresent();
        // Check response with country
        Assertions.assertEquals(httpResponse.getBody(), "Country with id "+oldCountry.getId()+" has been updated");
    }


    //TEST Community
    /**
     * Check Community Controller context
     */
    @Test
    public void CommunityControllerControllerContexLoad() {
        assertThat(communityController).isNotNull();
    }


    //TEST City

    /**
     * Check City Controller context
     */
    @Test
    public void CityControllerControllerContexLoad() {
        assertThat(cityController).isNotNull();
    }


    //TEST Player
    /**
     * Check Player Controller context
     */
    @Test
    public void PlayerControllerControllerContexLoad() {
        assertThat(playerController).isNotNull();
    }


    //TEST Team
    /**
     * Check Player Controller context
     */
    @Test
    public void TeamControllerControllerContexLoad() {
        assertThat(teamController).isNotNull();
    }


    @Test //Test insert a country
    void testInsertCountry() {
        //Check class
        Country country1 = countryRepository.save(new Country("América"));
        //Check name
        assert country1.getName().equals("América");
        //Check repository
        assert countryRepository.findById(country1.getId()).isPresent();
    }

    @Test //test get
    void getCountry() {
        Optional<Country> optionalCountry1 = countryRepository.findCountryByName("América");
        //first check if country is present
        assert optionalCountry1.isPresent();
        //check get
        assert optionalCountry1.get().getName().equals("América");
    }

    @Test
    public void testGetOrderByClicksCountry() throws Exception{
        //Check repository method
        ArrayList<Country> countrysOrdered = countryRepository.getOrderByClicksCountry();
        assert ! countrysOrdered.isEmpty();
    }

}
