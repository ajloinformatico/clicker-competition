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
     * I have created a method in player because I repeat the same block in
     * two controllers addSimple addPlayerWithCity
     * @param player {Player} player to add
     * @return {boolean} if player is saved return true
     */
    public boolean addPlayerCheck(Player player) throws java.io.IOException{
        //TODO: ask why it doesn't call the constructor
        player.setName(StringManagement.capitalize(player.getName()));
        player.setSurname(StringManagement.capitalize(player.getSurname()));
        player.setAvatar("./images/default.png");
        player.setPassword(new BCryptPasswordEncoder().encode(player.getPassword()));

        Optional<Player> oldPlayerByNames = playerRep.findPlayerByNameAndSurname(player.getName(), player.getSurname());
        Optional<Player> oldPlayerByMail = playerRep.findPlayerByMail(player.getMail());
        // if the player exists
        return oldPlayerByNames.isPresent() || oldPlayerByMail.isPresent();
    }


}
