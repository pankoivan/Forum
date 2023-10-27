package org.forum.main.repositories;

import org.forum.main.entities.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BanRepository extends JpaRepository<Ban, Integer> {

    List<Ban> findAllByEndDateAfter(LocalDate date);

    List<Ban> findAllByUserId(Integer id);

    List<Ban> findAllByUserWhoAssignedId(Integer id);

}
