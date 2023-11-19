package org.forum.main.repositories;

import org.forum.main.entities.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DislikeRepository extends JpaRepository<Dislike, Long> {

    Optional<Dislike> findByMessageIdAndUserId(Long messageId, Integer userId);

}
