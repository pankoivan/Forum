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

    List<User> findAllByOrderByNicknameDesc();

    List<User> findAllByOrderByRegistrationDateAsc();

    List<User> findAllByOrderByRegistrationDateDesc();

    List<User> findAllByRoleId(Integer roleId);

    List<User> findAllByRoleIdOrderByNicknameAsc(Integer roleId);

    List<User> findAllByRoleIdOrderByNicknameDesc(Integer roleId);

}
