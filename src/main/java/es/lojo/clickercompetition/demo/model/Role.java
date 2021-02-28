package es.lojo.clickercompetition.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This table is necessary to manage application permissions
 * President -> Can create new Teams or delete
 * Coach -> Can change team image and and and delete user of a team
 * Player -> Only clicks
 */
@Data
@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;

    // One to Many -> Player
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    Set<Player> players = new HashSet<>();

    public Role(){}

    public Role(String name){
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }





}
