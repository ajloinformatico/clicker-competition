package es.lojo.clickercompetition.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import es.lojo.clickercompetition.demo.utilities.StringManagement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Country {
    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private long clicks; //this attribute is only to store the sum of the clicks

    @JsonBackReference //Its it is necessary for the Json response to return in correct format
    @EqualsAndHashCode.Exclude //remove authorization from equals to avoid errors
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    Set<AuthonomusCommunity> fromCountry = new HashSet<>();

    public void setCapitalizedName(){
        this.name = StringManagement.capitalize(this.name);
    }

    public Country() {}

    public Country(String name){
        this.name = name;
        this.setCapitalizedName();
    }


    /**
     * this attribute is only to store the sum of the clicks
     * result of making a query with a sum about clicks
     * @param clicks {clicks long}:
     */
    public Country(Long id, String name, long clicks){
        this.id = id;
        this.name = name;
        this.clicks = clicks;
        this.setCapitalizedName();
    }
}
