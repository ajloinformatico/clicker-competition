package es.lojo.clickercompetition.demo.controllers;

import es.lojo.clickercompetition.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController {


    @Autowired
    private TeamRepository teamRepo;

    //Todo get order teams by clicks


}
