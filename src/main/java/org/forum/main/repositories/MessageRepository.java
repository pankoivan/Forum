package org.forum.main.repositories;

import org.forum.main.entities.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByTopicId(Integer topicId);

    List<Message> findAllByTopicId(Integer topicId, Sort sort);

}
