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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
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
        Country country =  countryRepository.save(new Country("Islandia"));
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

    /**
     * test allCommunitieslist
     */
    @Test
    public void testAllCommunitiesList(){
        // Check HttpCode
        ResponseEntity<Object> httpResponse = communityController.allCommunitieslist();
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response body is correct
        Assertions.assertEquals(httpResponse.getBody(), communityRepository.findAll());
    }

    /**
     * test allCommunitieslist
     */
    @Test
    public void testAllCommunitieslist(){
        //Check HttpCode
        ResponseEntity<Object> httpResponse = communityController.allCommunitiesListOrder();
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response body is correct
        Assertions.assertEquals(httpResponse.getBody(), communityRepository.allComunitiesListOrder());
    }

    /**
     * Check getOneCommunity
     */
    @Test
    public void testGetOneCommunity(){
        //Check HttpCode
        AuthonomusCommunity community = communityRepository.save(new AuthonomusCommunity("Catalunya"));
        ResponseEntity<Object> httpResponse = communityController.getOneCommunity(community.getId());
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check repo find country
        assertThat(communityRepository.findAuthonomusCommunityByName(community.getName())).isPresent();
        // Check response body is correct with community
        Assertions.assertEquals(httpResponse.getBody(), community);
    }

    /**
     * Check add Community
     */
    @Test
    public void testAddSimpleCommunity(){
        AuthonomusCommunity community = new AuthonomusCommunity("Islandia");
        ResponseEntity<Object> httpResponse = communityController.communityAddSimple(community);
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response with community
        Assertions.assertEquals(httpResponse.getBody(), "Autonomous Community with name "+community.getName());
    }

    /**
     * Check delete community
     */
    @Test
    public void testDeleteCommunity(){
        AuthonomusCommunity community =  communityRepository.save(new AuthonomusCommunity("Malaga"));
        ResponseEntity<Object> httpResponse = communityController.deleteAutonomousCommunity(community.getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        //Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response with community
        Assertions.assertEquals(httpResponse.getBody(), "Autonomous Community with id "+community.getId()+" has been deleted");
    }

    /**
     * Check update community
     */
    @Test
    public void updateCommunity(){
        AuthonomusCommunity oldCommunity = communityRepository.save(new AuthonomusCommunity("Malaga"));
        AuthonomusCommunity newCommunity = new AuthonomusCommunity("infolojosCountry");
        ResponseEntity<Object> httpResponse = communityController.updateCommunity(oldCommunity.getId(), newCommunity);
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        //Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        //Check data
        assert communityRepository.findAuthonomusCommunityByName(newCommunity.getName()).isPresent();
        // Check response with community
        Assertions.assertEquals(httpResponse.getBody(), "Autonomus Community with id "+oldCommunity.getId()+" has been update");
    }


    //TEST City

    /*
    * allCitiesList
    */
    @Test
    public void testAllCitiesLis() {
        // Check HttpCode
        ResponseEntity<Object> httpResponse = cityController.allCitiesList();
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response body is correct
        Assertions.assertEquals(httpResponse.getBody(), cityRepository.findAll());
    }

    /**
     * Test allCityListOrder
     */
    @Test
    public void testAllCityListOrder(){
        //Check HttpCode
        ResponseEntity<Object> httpResponse = cityController.allCityListOrder();
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response body is correct
        Assertions.assertEquals(httpResponse.getBody(), cityRepository.cityListAllOrder());
    }

    /**
     * test getOneCity
     */
    @Test
    public void testGetOneCity(){
        City city = cityRepository.save(new City("Aburgo"));
        ResponseEntity<Object> httpResponse = cityController.getOnCity(city.getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check repo find country
        assertThat(cityRepository.findById(city.getId())).isPresent();
        // Check response with city
        Assertions.assertEquals(httpResponse.getBody(), city);
    }

    /**
     * test cityAddSimple
     */
    @Test
    public void testCityAddSimple(){
        City city = new City("Islandia");
        ResponseEntity<Object> httpResponse = cityController.cityAddSimple(city);
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response with city
        Assertions.assertEquals(httpResponse.getBody(), "City with name "+city.getName()+" has been registered");
    }

    /**
     * test deleteCity
     */
    @Test
    public void testDeleteCity(){
        City city = cityRepository.save(new City("Asburgo"));
        ResponseEntity<Object> httpResponse = cityController.deleteCity(city.getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response with city
        Assertions.assertEquals(httpResponse.getBody(), "City with id "+city.getId()+" has been deleted");
    }

    /**
     * test updateCity
     */
    @Test
    public void tesUpdateCity(){
        City oldCity = cityRepository.save(new City("Asburgo"));
        City newCity = new City("infolojosCity");
        ResponseEntity<Object> httpResponse = cityController.updateCity(newCity, oldCity.getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        //Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        //Check data
        assert cityRepository.findCityByName(newCity.getName()).isPresent();
        // Check response with city
        Assertions.assertEquals(httpResponse.getBody(), "City with id "+oldCity.getId()+" has been updated");
    }


    //TEST PLAYER

    /*
     * test all players
     */
    @Test
    public void testAllPlayerLis() {
        // Check HttpCode
        ResponseEntity<Object> httpResponse = playerController.allPlayersList();
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response body is correct
        Assertions.assertEquals(httpResponse.getBody(), playerRepository.findAll());
    }

    /**
     * Test allPlayersListOrder
     */
    @Test
    public void testAllPlayerListOrder(){
        //Check HttpCode
        ResponseEntity<Object> httpResponse = playerController.allPlayerListOrder();
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response body is correct
        Assertions.assertEquals(httpResponse.getBody(), playerRepository.allPlayersListOrder());
    }

    /**
     * test getOnePlayer
     */
    @Test
    public void testGetOnePlayer(){
        Player player = playerRepository.findPlayerByName("Alex").orElseThrow(()->new EntityNotFoundException("Alex"));
        ResponseEntity<Object> httpResponse = playerController.getOnePlayer(player.getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check repo find country
        assertThat(playerRepository.findById(player.getId())).isPresent();
        // Check response with player
        Assertions.assertEquals(httpResponse.getBody(), player);
    }
    //public Player(String name, String surname, String password, int edad, int clicks, String mail, City city, Role role){

    /**
     * test playerAddSimple
     */
    @Test
    public void testPlayerAddSimple() throws IOException {
        Role rol = roleRepository.findRoleByName("player").orElseThrow(()->new EntityNotFoundException("player"));
        Player player = new Player("Raul","Lojo","pestillo01",21,22,
                "raul@raul.com");
        player.setRole(rol);

        ResponseEntity<Object> httpResponse = playerController.playerAddSimple(player);
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response with player
        Assertions.assertEquals(httpResponse.getBody(), "Player with name " + player.getName() + " has been registered ");
        playerRepository.delete(playerRepository.findPlayerByName(player.getName()).get());
    }

    /**
     * Check update Player with
     * with this method the image is also updated
     * but I decided to separate it into different requests
     */
    @Test
    public void testPlayerUpdate() throws IOException {
        Optional<Player> oldPlayer = playerRepository.findPlayerByName("Juan");
        assertThat(oldPlayer).isPresent();
        Player newPlayer = new Player("Guillermo","Barroso","pestillo01",34,23,"juan@juan.es");
        ResponseEntity<Object> httpResponse = playerController.updatePlayer(newPlayer, oldPlayer.get().getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response with player
        Assertions.assertEquals(httpResponse.getBody(), "Player with id " + oldPlayer.get().getId() + " has been updated");
    }

    /**
     * test deletePlayer
     */
    @Test
    public void testDeletePlayer(){
        Player player = playerRepository.save(new Player("Raul", "Martínez", "pestillo01", 34, 0, "raul@raul.es"));
        ResponseEntity<Object> httpResponse = playerController.deletePlayer(player.getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        // Check response with city
        Assertions.assertEquals(httpResponse.getBody(), "Player with id "+player.getId()+" has been deleted");
    }

    /**
     * Test update clicks
     */
    @Test
    public void testUpdateClicks() throws IOException {
        Player player = playerRepository.save(new Player("Benito", "Bendito", "pestillo01", 34, 0, "benito@benito.es"));
        //Check player has not clicks
        assertThat(player.getClicks()).isEqualTo(0);
        ResponseEntity<Object> httpResponse = playerController.UpdateClicks(player.getId());
        //Check HttpCode
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
        // Check response body is not null
        Assertions.assertNotNull(httpResponse.getBody());
        //Check cliks updated
        Optional<Player> playerUpdated = playerRepository.findById(player.getId());
        assertThat(playerUpdated).isPresent();
        assertThat(playerUpdated.get().getClicks()).isEqualTo(30);

        // Check response with city
        Assertions.assertEquals(httpResponse.getBody(), player.getId()+" - "+playerUpdated.get().getClicks());
    }

    /**
     * Test updateCity
     */
    @Test
    public void testUpdatePlayerCity() {
        Optional<Player> player = playerRepository.findPlayerByName("Benito");
        assertThat(player).isPresent();
        Optional<City> city = cityRepository.findCityByName("El puerto");
        assertThat(player).isPresent();

    }







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
