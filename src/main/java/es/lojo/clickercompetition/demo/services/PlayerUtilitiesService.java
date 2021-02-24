package es.lojo.clickercompetition.demo.services;

import es.lojo.clickercompetition.demo.model.Player;
import es.lojo.clickercompetition.demo.repository.PlayerRepository;
import es.lojo.clickercompetition.demo.utilities.StringManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerUtilitiesService {


    @Autowired
    private PlayerRepository playerRep;


    /**
     * This check is only done in login for what we was speaking!!
     * I have created a method in player because I repeat the same block in
     * two controllers addSimple addPlayerWithCity
     * @param player {Player} player to add
     * @return {boolean} if player is saved return true
     */
    public boolean addPlayerCheck(Player player) throws java.io.IOException{
        //TODO: ask why it doesn't call the constructor
        player.setCapitalizedNames(); //Set name and Surname capitalizes
        player.setUserDefaultAvatar(); //Set avatar
        player.setEncriptedPassword(); //Set password

        Optional<Player> oldPlayerByNames = playerRep.findPlayerByNameAndSurname(player.getName(), player.getSurname());
        Optional<Player> oldPlayerByMail = playerRep.findPlayerByMail(player.getMail());
        // if the player exists
        return oldPlayerByNames.isPresent() || oldPlayerByMail.isPresent();
    }


}
