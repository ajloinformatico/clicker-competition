package es.lojo.clickercompetition.demo;

import static org.assertj.core.api.Assertions.assertThat;
import es.lojo.clickercompetition.demo.bootstrap.Seeder;
import es.lojo.clickercompetition.demo.controllers.CountryController;
import es.lojo.clickercompetition.demo.model.*;
import es.lojo.clickercompetition.demo.repository.*;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
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

    // GET REPOSITORIES
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

    //GET CONTROLLERS
    @Autowired
    private CountryController countryController;


    //TEST COUNTRY

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
    public void CountryControllerContexLoad() throws Exception {
        assertThat(countryController).isNotNull();
    }

    @Ignore
    @Test
    void seederSelectsClicks(){
        //Countries
        Country spain = countryRepository.save(new Country("Spain"));
        Country andorra = countryRepository.save(new Country("Andorra"));
        Country iceland = countryRepository.save(new Country("Iceland"));
        Country italy = countryRepository.save(new Country("Italy"));

        //AuthonomusCommunity
        AuthonomusCommunity andalucia = communityRepository.save(new AuthonomusCommunity("Andalucia", spain));
        AuthonomusCommunity madrid = communityRepository.save(new AuthonomusCommunity("Madrid", spain));
        AuthonomusCommunity barcelona = communityRepository.save(new AuthonomusCommunity("Barcelona", spain));
        AuthonomusCommunity galicia = communityRepository.save(new AuthonomusCommunity("Galicia", spain));
        AuthonomusCommunity iceland1 = communityRepository.save(new AuthonomusCommunity("Akranes", iceland));
        AuthonomusCommunity iceland2 = communityRepository.save(new AuthonomusCommunity("Iceland 2", iceland));
        AuthonomusCommunity icelnad3 = communityRepository.save(new AuthonomusCommunity("Iceland3", iceland));
        AuthonomusCommunity abruzos = communityRepository.save(new AuthonomusCommunity("Abruzos", italy));
        AuthonomusCommunity apuila = communityRepository.save(new AuthonomusCommunity("Apuila", italy));
        AuthonomusCommunity andorraComunidad = communityRepository.save(new AuthonomusCommunity("Andorra", andorra));

        //City
        City elPuerto = cityRepository.save(new City("El Puerto", andalucia));
        City campoReal = cityRepository.save(new City("Campo Real", madrid));
        City barcelonaCity = cityRepository.save(new City("Barcelona", barcelona));
        City galiciaCity = cityRepository.save(new City("Galicia", galicia));
        City onotro = cityRepository.save(new City("Onotro", iceland1));
        City icelandCity2 = cityRepository.save(new City("Icelandcity2", iceland2));
        City icelandCity3 = cityRepository.save(new City("Icelandcity3", icelnad3));
        City pizza = cityRepository.save(new City("Pizza", abruzos));
        City bari = cityRepository.save(new City("Bari", apuila));
        City andorraCity = cityRepository.save(new City("Andorra", andorraComunidad));

        //se que no es necesario en team y player porque no hay tablas que requieran de ellas
        // pero instancio por mantener coherencia


        //Roles
        Role player = roleRepository.save(new Role("player"));
        Role coach = roleRepository.save(new Role("coach"));
        Role president = roleRepository.save(new Role("president"));


        //Player
        Player juan = playerRepository.save(new Player("Juan", "Lopez", "pestillo01", 34, 0, "juan@juan.es",
                elPuerto, player));
        Player diego = playerRepository.save(new Player("Diego", "Alcántara", "pestillo01", 19, 90, "deigo@diego.es",
                andorraCity, coach));
        Player elena = playerRepository.save(new Player("Elena", "Rosendo", "pestillo01", 20, 4, "elena@elena.es",
                pizza, president));
        Player alex = playerRepository.save(new Player("Alex", "Gwan", "pestillo01", 34, 34, "alex@alex.com",
                onotro, president));
        Player tim = playerRepository.save(new Player("Tim", "wistron", "pestillo01", 21, 210, "tim@tim.ir",
                icelandCity2, player));
        Player atom = playerRepository.save(new Player("Atom", "Visual", "pestillo01", 34, 40, "tim@tim.es",
                icelandCity3, player));


        Team reyesDelClick = new Team("Reyes del Click");
        Team blueDragonClickMaster = new Team("Blue Dragon Click master");
        Team clickRedTeam = new Team("Click red team");
        Team clickeame = new Team("Clickeame");




        //add players to Teams and update clicks
        reyesDelClick.addPlayersList(new ArrayList<Player>(Arrays.asList(juan, diego)));
        reyesDelClick.updateCliks();
        blueDragonClickMaster.addPlayer(elena);
        blueDragonClickMaster.updateCliks();
        clickRedTeam.addPlayer(alex);
        clickRedTeam.updateCliks();
        clickeame.addPlayersList(new ArrayList<Player>(Arrays.asList(tim, atom)));
        clickeame.updateCliks();
        //List<Team> teamsList = new ArrayList<Team>(Arrays.asList(reyesDelClick, blueDragonClickMaster, clickRedTeam, clickeame));
        teamRepository.saveAll(new ArrayList<Team>(Arrays.asList(reyesDelClick, blueDragonClickMaster, clickRedTeam, clickeame)));
    }

    @Test
    public void testGetOrderByClicksCountry() throws Exception{

        //Check repository method
        ArrayList<Country> countrysOrdered = countryRepository.getOrderByClicksCountry();
        assert ! countrysOrdered.isEmpty();

        //Check controller
        assertThat(countryController).isNotNull();
    }

}
