package es.lojo.clickercompetition.demo.repository;

import es.lojo.clickercompetition.demo.model.City;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author: antoniojoselojoojeda
 * City Repository
 */
public interface CityRepository extends CrudRepository<City, Long> {


    /**
     * Returns the city if it exists
     * @param name {String}: City name
     * @return {City} if city exists
     */
    Optional<City> findCityByName(String name);



    /**
     * Execute
     * SELECT c.id, c.name, (pl.clicks) FROM city c
     * JOIN player pl ON c.id = pl.city_id
     * GROUP BY c.id order by sum(pl.clicks) desc;
     * @return {ArrayList<Object>}: Return all cities names with sum() of clicks
     */
    @Query("SELECT new es.lojo.clickercompetition.demo.model.City(c.id, c.name, sum(pl.clicks)) " +
            " FROM City c " +
            " JOIN Player pl ON c.id = pl.city.id" +
            " GROUP BY c.id ORDER BY SUM(pl.clicks) DESC")
    public ArrayList<City> cityListAllOrder();

}
