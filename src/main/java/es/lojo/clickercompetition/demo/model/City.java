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
public class City {
    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private Long clicks;

    // Much to One -> AutonomusCommunity
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn()
    private AuthonomusCommunity authonomusCommunity;

    // One to Many -> Player
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    Set<Player> fromCity = new HashSet<>();

    public City() {}

    public City(String name){
        this.name = StringManagement.capitalize(name);
    }

    public City(String name, AuthonomusCommunity authonomusCommunity){
        this.name = StringManagement.capitalize(name);
        this.authonomusCommunity = authonomusCommunity;
    }


    public City(Long id, String name, Long cliks){
        this.id = id;
        this.name = StringManagement.capitalize(name);
        this.clicks = cliks;
    }
}