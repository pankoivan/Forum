package org.forum.main.repositories;

import org.forum.main.entities.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {

    Optional<Dislike> findByMessageIdAndUserId(Long messageId, Integer userId);

}
