package es.lojo.clickercompetition.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import es.lojo.clickercompetition.demo.model.Country;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author antoniojoselojoojeda
 * AutonomusCommunity model has M:1 -> City
 * AutonomusCommunity model has M:1 -> Country
 * annotations explained on Player model
 */
@Entity
@Data
public class AuthonomusCommunity {
    @Id @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String name;

    private Long clicks;


    //Much to One -> Country
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn()
    private Country country;

    //One to Much -> City
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "authonomusCommunity", cascade = CascadeType.ALL)
    Set<City> fromAutonomusCommunity = new HashSet<>();


    //Empty Constructor
    public AuthonomusCommunity() {}

    public AuthonomusCommunity(String name){
        this.name = name;
    }

    public AuthonomusCommunity(String name, Country country){
        this.name = name;
        this.country = country;
    }

    public AuthonomusCommunity(Long id, String name, Long clicks) {
        this.id = id;
        this.name = name;
        this.clicks = clicks;
    }
}