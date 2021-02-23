package es.lojo.clickercompetition.demo.model;


import es.lojo.clickercompetition.demo.utilities.StringManagement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;


import javax.persistence.*;
@Entity
@Data
public class Player {

    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String surname;
    private String password;
    private int edad;
    private int clicks; //clicks de cada usuario

    //mail must be unique
    @Column(unique = true)
    private String mail;
    private String avatar; //url profile imagen

    //Many to one -> City
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn()
    private City city;

    //Many to many -> Team
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "players") //tiene que tener mapped by del otro
            Set<Team> teams = new HashSet<>();


    public Player() {}

    //TODO: In player controller don't call constructor
    //img will be insert after crete
    public Player(String name, String surname, String password, int edad, int clicks, String mail){
        this.name =  StringManagement.capitalize(name);
        this.surname = StringManagement.capitalize(surname);
        this.password = new BCryptPasswordEncoder().encode(password);
        this.edad = edad;
        this.clicks = clicks;
        this.mail = mail;
        this.avatar = "./images/default.png";
    }


    //img will be insert after crete
    public Player(String name, String surname, String password, int edad, int clicks, String mail, City city){
        this.name = name;
        this.surname = surname;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.edad = edad;
        this.clicks = clicks;
        this.mail = mail;
        this.city = city;
        this.avatar = "./images/default.png";
    }

    public Player(String name, String surname, String password, int edad, int clicks, String mail, City city, String avatar){
        this.name = name;
        this.surname = surname;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.edad = edad;
        this.clicks = clicks;
        this.mail = mail;
        this.city = city;
        this.avatar = avatar;
    }


}
