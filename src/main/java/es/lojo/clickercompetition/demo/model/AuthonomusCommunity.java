package es.lojo.clickercompetition.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import es.lojo.clickercompetition.demo.utilities.StringManagement;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class AuthonomusCommunity {
    @Id
    @Getter @Setter
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "authonomusCommunity", cascade = CascadeType.ALL)
    Set<City> fromAutonomusCommunity = new HashSet<>();

    public void setCapitalizedName(){
        this.name = StringManagement.capitalize(this.name);
    }

    //Empty Constructor
    public AuthonomusCommunity() {}

    public AuthonomusCommunity(String name){
        this.name = name;
        setCapitalizedName();
    }

    public AuthonomusCommunity(String name, Country country){
        this.name = name;
        this.country = country;
        setCapitalizedName();
    }

    public AuthonomusCommunity(Long id, String name, Long clicks) {
        this.id = id;
        this.name = name;
        this.clicks = clicks;
    }
}
