package es.lojo.clickercompetition.demo.repository;

import es.lojo.clickercompetition.demo.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


/**
 * @author antoniojoselojoojeda
 * Role Repository
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    public Optional<Role> findRoleByName(String name);

}