package org.forum.repositories;

import org.forum.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    boolean existsByName(String name);

    Optional<Role> findByName(String name);

    List<Role> findAllByOrderByNameAsc();

    List<Role> findAllByOrderByNameDesc();

}
