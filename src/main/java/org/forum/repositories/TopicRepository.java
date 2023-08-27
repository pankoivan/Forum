package org.forum.repositories;

import org.forum.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    boolean existsByName(String name);

    boolean existsByDescription(String name);

    List<Topic> findAllBySectionId(Integer sectionId);

}
