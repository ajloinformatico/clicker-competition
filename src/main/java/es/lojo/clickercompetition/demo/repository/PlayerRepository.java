package es.lojo.clickercompetition.demo.repository;

import es.lojo.clickercompetition.demo.model.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author: antoniojoselojoojeda
 * Player Repository
 */
public interface PlayerRepository extends CrudRepository<Player, Long> {

    /**
     * return Player by name and surname
     * @param name {String}: player name
     * @param surname {String}: player surname
     * @return Player: player
     */
    Optional<Player> findPlayerByNameAndSurname(String name, String surname);


    Optional<Player> findPlayerByMail(String mail);

    /**
     * select all players ordered by clicks
     * @return {Player ArrayList}
     *
     */
    @Query("SELECT pl FROM Player pl ORDER BY pl.clicks DESC")
    public ArrayList<Player> allPlayersListOrder();

}
