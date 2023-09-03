package org.forum.repositories;

import org.forum.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    boolean existsByName(String name);

    boolean existsByDescription(String name);

    Optional<Section> findByName(String name);

    Optional<Section> findByDescription(String description);

}
