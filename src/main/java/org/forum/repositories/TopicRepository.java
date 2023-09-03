package org.forum.repositories;

import org.forum.entities.Topic;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    boolean existsByName(String name);

    boolean existsByDescription(String name);

    Optional<Topic> findByName(String name);

    Optional<Topic> findByDescription(String description);

    List<Topic> findAllBySectionId(Integer sectionId);

    List<Topic> findAllBySectionId(Integer sectionId, Sort sort);

}
