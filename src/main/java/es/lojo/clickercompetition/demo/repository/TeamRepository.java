package es.lojo.clickercompetition.demo.repository;


import es.lojo.clickercompetition.demo.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author antoniojoselojoojeda
 */
public interface TeamRepository extends CrudRepository<Team, Long> {

    /**
     * Get a team by unique name
     * @param name {String}: Team name
     * @return {Optional}: Optional team
     */
    public Optional<Team> findTeamByName(String name);




    @Query("SELECT new es.lojo.clickercompetition.demo.model.Team(id, name, clicks) " +
            "FROM Team ORDER BY clicks DESC ")
    public ArrayList<Team> findAllTeamssOrder();
}
