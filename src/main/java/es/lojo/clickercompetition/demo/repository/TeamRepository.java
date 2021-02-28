package es.lojo.clickercompetition.demo.repository;

import es.lojo.clickercompetition.demo.model.City;
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
    public Optional<Team> getTeamsByName(String name);


    /**
     * Get all teams order by id
     * @return {ArrayList<Team>}: Array of teams
     */
    @Query("SELECT new es.lojo.clickercompetition.demo.model.Team(t.id, t.name, sum(pl.clicks)) " +
            " FROM Team t " +
            " JOIN Player pl ON t.id = pl.city.id" +
            " GROUP BY t.id ORDER BY SUM(pl.clicks) DESC")
    public ArrayList<City> TeamsListAllOrder();



}
