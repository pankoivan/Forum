package org.forum.repositories;

import org.forum.entities.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BanRepository extends JpaRepository<Ban, Integer> {

    List<Ban> findAllByEndDateAfter(LocalDate date);

}
