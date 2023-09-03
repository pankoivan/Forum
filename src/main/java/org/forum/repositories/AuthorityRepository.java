package org.forum.repositories;

import org.forum.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    boolean existsByName(String name);

}
