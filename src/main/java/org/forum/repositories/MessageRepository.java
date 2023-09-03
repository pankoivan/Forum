package org.forum.repositories;

import org.forum.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByOrderByCreationDateAsc();

    List<Message> findAllByOrderByCreationDateDesc();

    List<Message> findAllByTopicId(Integer topicId);

    List<Message> findAllByTopicIdOrderByCreationDateAsc(Integer topicId);

    List<Message> findAllByTopicIdOrderByCreationDateDesc(Integer topicId);

}
