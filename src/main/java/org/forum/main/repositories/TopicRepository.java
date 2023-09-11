package org.forum.main.repositories;

import org.forum.main.entities.Topic;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = """
            SELECT t FROM Topic t
            LEFT JOIN t.messages m
            GROUP BY t.id
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(m.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(m.id) END DESC
            """)
    List<Topic> findAllByOrderByMessagesCountWithDirection(@Param("direction") String direction);

    @Query(value = """
            SELECT t FROM Topic t
            LEFT JOIN t.messages m
            WHERE t.section.id = :sectionId
            GROUP BY t.id
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(m.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(m.id) END DESC
            """)
    List<Topic> findAllBySectionIdOrderByMessagesCountWithDirection(@Param("sectionId") Integer sectionId,
                                                                    @Param("direction") String direction);

}
