package org.forum.main.repositories;

import org.forum.main.entities.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByTopicId(Integer topicId);

    List<Message> findAllByTopicId(Integer topicId, Sort sort);

    @Query(value = """
            SELECT m FROM Message m
            LEFT JOIN m.likedUsers lu
            GROUP BY m.id
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(lu.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(lu.id) END DESC
            """)
    List<Message> findAllByOrderByLikesCountWithDirection(@Param("direction") String direction);

    @Query(value = """
            SELECT m FROM Message m
            LEFT JOIN m.likedUsers lu
            WHERE m.topic.id = :topicId
            GROUP BY m.id
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(lu.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(lu.id) END DESC
            """)
    List<Message> findAllByTopicIdOrderByLikesCountWithDirection(@Param("topicId") Integer topicId,
                                                                 @Param("direction") String direction);

}
