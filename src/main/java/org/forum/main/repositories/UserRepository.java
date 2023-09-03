package org.forum.main.repositories;

import org.forum.main.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findAllByRoleId(Integer roleId);

    List<User> findAllByRoleId(Integer roleId, Sort sort);

}
