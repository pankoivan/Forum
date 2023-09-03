package org.forum.main.repositories;

import org.forum.main.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    boolean existsByName(String name);

}
