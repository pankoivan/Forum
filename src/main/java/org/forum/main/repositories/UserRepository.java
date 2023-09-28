package org.forum.main.repositories;

import org.forum.main.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(pm.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(pm.id) END DESC
            """)
    List<User> findAllByOrderByMessagesCountWithDirection(@Param("direction") String direction);

    @Query(value = """
            SELECT u FROM User u
            LEFT JOIN u.postedMessages pm
            LEFT JOIN pm.likedUsers lu
            LEFT JOIN pm.dislikedUsers du
            GROUP BY u.id
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(lu.id) - COUNT(du.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(lu.id) - COUNT(du.id) END DESC
            """)
    List<User> findAllByOrderByReputationWithDirection(@Param("direction") String direction);

    @Query(value = """
            SELECT u FROM User u
            LEFT JOIN u.postedMessages pm
            WHERE u.role.name = :roleName
            GROUP BY u.id
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(pm.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(pm.id) END DESC
            """)
    List<User> findAllByRoleNameOrderByMessagesCountWithDirection(@Param("roleName") String roleName,
                                                                  @Param("direction") String direction);

    @Query(value = """
            SELECT u FROM User u
            LEFT JOIN u.postedMessages pm
            WHERE u.role.name = :roleName
            GROUP BY u.id
            ORDER BY
                CASE WHEN :direction = 'ASC' THEN COUNT(pm.id) END ASC,
                CASE WHEN :direction = 'DESC' THEN COUNT(pm.id) END DESC
            """)
    List<User> findAllByRoleNameOrderByReputationWithDirection(@Param("roleName") String roleName,
                                                               @Param("direction") String direction);

}
