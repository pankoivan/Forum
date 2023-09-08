package org.forum.main.repositories;

import org.forum.main.entities.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
            """)
    List<Message> findAllJoinedToLikesGroupedByMessageId(JpaSort sortBy);

}
