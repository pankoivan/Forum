package org.forum.repositories;

import org.forum.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByNicknameAsc();

    List<User> findAllByRoleIdOrderByNicknameAsc(Integer roleId);

    List<User> findAllByRoleId(Integer roleId);

}
