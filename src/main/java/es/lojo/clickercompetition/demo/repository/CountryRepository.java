package es.lojo.clickercompetition.demo.repository;

import es.lojo.clickercompetition.demo.model.Country;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * @author antoniojoselojoojeda
 * Country repository
 */
public interface CountryRepository extends CrudRepository<Country, Long> {

    //Example findAllby --> defined by default

    /**
     * Execute :
     * SELECT co.id, co.name, sum(pl.clicks) FROM country co
     * JOIN authonomus_community au ON co.id = au.country_id
     * JOIN city c ON au.id = c.authonomus_community_id
     * JOIN  player pl  ON c.id = pl.city_id
     * GROUP BY co.id order by sum(pl.clicks)  desc;
     *
     * @return ArrayList of country and clicks
     */
    @Query("SELECT new es.lojo.clickercompetition.demo.model.Country(c.id , co.name, SUM(pl.clicks)) " +
            "from Country co " +
            "JOIN AuthonomusCommunity au ON co.id = au.country.id " +
            "JOIN City c ON au.id = c.authonomusCommunity.id " +
            "JOIN  Player pl  ON c.id = pl.city.id " +
            "GROUP BY co.id ORDER BY SUM(pl.clicks) DESC ")
    public ArrayList<Country> getOrderByClicksCountry();




}
