package org.forum.main.repositories;

import org.forum.main.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMessageIdAndUserId(Long messageId, Integer userId);

}
