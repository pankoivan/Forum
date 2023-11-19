package org.forum.main.repositories;

import org.forum.main.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMessageIdAndUserId(Long messageId, Integer userId);

}
