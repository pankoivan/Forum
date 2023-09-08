package org.forum.main.repositories;

import org.forum.main.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findAllByRoleName(String roleName);

    List<User> findAllByRoleName(String roleName, Sort sort);

    @Query(value = """
            SELECT u FROM User u
            LEFT JOIN u.postedMessages pm
            GROUP BY u.id
            """)
    List<User> findAllJoinedToMessagesGroupedByUserId(JpaSort orderBy);

    @Query(value = """
            SELECT u FROM User u
            LEFT JOIN u.postedMessages pm
            LEFT JOIN pm.likedUsers lu
            GROUP BY u.id
            """)
    List<User> findAllJoinedToMessagesJoinedToLikesGroupedByUserId(JpaSort orderBy);

}
