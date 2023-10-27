package org.forum.main.repositories;

import org.forum.main.entities.Ban;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BanRepository extends JpaRepository<Ban, Integer> {

    List<Ban> findAllByEndDateAfter(LocalDate date);

    List<Ban> findAllByUserId(Integer id);

    List<Ban> findAllByUserWhoAssignedId(Integer id);

    List<Ban> findAllByUserId(Integer id, Sort sort);

    List<Ban> findAllByUserWhoAssignedId(Integer id, Sort sort);

    /*List<Ban> findAllByOrderByUserNickname(Sort sort);

    List<Ban> findAllByOrderByUserWhoAssignedNickname(Sort sort);*/

}
