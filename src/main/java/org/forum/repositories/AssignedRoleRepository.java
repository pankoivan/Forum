package org.forum.repositories;

import org.forum.entities.AssignedRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignedRoleRepository extends JpaRepository<AssignedRole, Integer> {

}
