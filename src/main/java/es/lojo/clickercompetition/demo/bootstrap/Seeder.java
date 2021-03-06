package es.lojo.clickercompetition.demo.bootstrap;

import es.lojo.clickercompetition.demo.model.*;
import es.lojo.clickercompetition.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;




@Component
public class Seeder implements CommandLineRunner {


    //Connect with repositories
    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private AuthonomusCommunityRepository authonomusCommunityRepo;

    @Autowired
    private CityRepository cityRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private RoleRepository roleRepository;



    //Execute on the run
    @Override
    public void run(String... args) throws Exception {
        //Countries
        Country spain = countryRepo.save(new Country("Spain"));
        Country andorra = countryRepo.save(new Country("Andorra"));
        Country iceland = countryRepo.save(new Country("Iceland"));
        Country italy = countryRepo.save(new Country("Italy"));

        //AuthonomusCommunity
        AuthonomusCommunity andalucia = authonomusCommunityRepo.save(new AuthonomusCommunity("Andalucia", spain));
        AuthonomusCommunity madrid = authonomusCommunityRepo.save(new AuthonomusCommunity("Madrid", spain));
        AuthonomusCommunity barcelona = authonomusCommunityRepo.save(new AuthonomusCommunity("Barcelona", spain));
        AuthonomusCommunity galicia = authonomusCommunityRepo.save(new AuthonomusCommunity("Galicia", spain));
        AuthonomusCommunity iceland1 = authonomusCommunityRepo.save(new AuthonomusCommunity("Akranes", iceland));
        AuthonomusCommunity iceland2 = authonomusCommunityRepo.save(new AuthonomusCommunity("Iceland 2", iceland));
        AuthonomusCommunity icelnad3 = authonomusCommunityRepo.save(new AuthonomusCommunity("Iceland3", iceland));
        AuthonomusCommunity abruzos = authonomusCommunityRepo.save(new AuthonomusCommunity("Abruzos", italy));
        AuthonomusCommunity apuila = authonomusCommunityRepo.save(new AuthonomusCommunity("Apuila", italy));
        AuthonomusCommunity andorraComunidad = authonomusCommunityRepo.save(new AuthonomusCommunity("Andorra", andorra));

        //City
        City elPuerto = cityRepo.save(new City("El Puerto", andalucia));
        City campoReal = cityRepo.save(new City("Campo Real", madrid));
        City barcelonaCity = cityRepo.save(new City("Barcelona", barcelona));
        City galiciaCity = cityRepo.save(new City("Galicia", galicia));
        City onotro = cityRepo.save(new City("Onotro", iceland1));
        City icelandCity2 = cityRepo.save(new City("Icelandcity2", iceland2));
        City icelandCity3 = cityRepo.save(new City("Icelandcity3", icelnad3));
        City pizza = cityRepo.save(new City("Pizza", abruzos));
        City bari = cityRepo.save(new City("Bari", apuila));
        City andorraCity = cityRepo.save(new City("Andorra", andorraComunidad));

        //se que no es necesario en team y player porque no hay tablas que requieran de ellas
        // pero instancio por mantener coherencia


        //Roles
        Role player = roleRepository.save(new Role("ROLE_player"));
        Role coach = roleRepository.save(new Role("ROLE_coach"));
        Role president = roleRepository.save(new Role("ROLE_president"));


        //Player
        Player juan = playerRepo.save(new Player("Juan", "Lopez", "pestillo01", 34, 0, "juan@juan.es",
                elPuerto, player));
        Player diego = playerRepo.save(new Player("Diego", "Alcántara", "pestillo01", 19, 90, "deigo@diego.es",
                andorraCity, coach));
        Player elena = playerRepo.save(new Player("Elena", "Rosendo", "pestillo01", 20, 4, "elena@elena.es",
                pizza, president));
        Player alex = playerRepo.save(new Player("Alex", "Gwan", "pestillo01", 34, 34, "alex@alex.com",
                onotro, president));
        Player tim = playerRepo.save(new Player("Tim", "wistron", "pestillo01", 21, 210, "tim@tim.ir",
                icelandCity2, player));
        Player atom = playerRepo.save(new Player("Atom", "Visual", "pestillo01", 34, 40, "tim@tim.es",
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
        teamRepo.saveAll(new ArrayList<Team>(Arrays.asList(reyesDelClick, blueDragonClickMaster, clickRedTeam, clickeame)));
    }
}

