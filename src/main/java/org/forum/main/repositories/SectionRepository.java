package org.forum.main.repositories;

import org.forum.main.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    boolean existsByName(String name);

    boolean existsByDescription(String name);

    Optional<Section> findByName(String name);

    Optional<Section> findByDescription(String description);

    @Query(value = """
            SELECT s FROM Section s
            LEFT JOIN s.topics t
            GROUP BY s.id
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(t.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(t.id) END DESC
            """)
    List<Section> findAllByOrderByTopicsCountWithDirection(@Param("direction") String direction);

    @Query(value = """
            SELECT s FROM Section s
            LEFT JOIN s.topics t
            LEFT JOIN t.messages m
            GROUP BY s.id
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(m.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(m.id) END DESC
            """)
    List<Section> findAllByOrderByMessagesCountWithDirection(@Param("direction") String direction);

}
