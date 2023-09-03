package org.forum.repositories;

import org.forum.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    boolean existsByName(String name);

    List<Authority> findAllByOrderByNameAsc();

}
