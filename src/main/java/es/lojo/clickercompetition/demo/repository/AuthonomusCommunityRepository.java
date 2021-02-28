package es.lojo.clickercompetition.demo.repository;

import es.lojo.clickercompetition.demo.model.AuthonomusCommunity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author antonio
 */
public interface AuthonomusCommunityRepository extends CrudRepository<AuthonomusCommunity, Long> {


    Optional<AuthonomusCommunity> findAuthonomusCommunityByName(String name);

    /**
     * SELECT au.id, au.name, pl.clicks FROM authonomus_community au
     * JOIN city c ON au.id = c.authonomus_community_id
     * JOIN  player pl  ON c.id = pl.city_id
     * order by pl.clicks  desc;
     *
     * @return {ArrayList} : List AuthonomusComunitu and clicks
     *
     */
    @Query("SELECT new es.lojo.clickercompetition.demo.model.AuthonomusCommunity(au.id, au.name, SUM(pl.clicks))" +
            " FROM AuthonomusCommunity  au" +
            " JOIN City c ON au.id = c.authonomusCommunity.id " +
            "JOIN Player pl ON c.id = pl.city.id" +
            " GROUP BY au.id ORDER BY SUM(pl.clicks) DESC ")
    public ArrayList<AuthonomusCommunity> allComunitiesListOrder();


}
