package org.example.repository;

import org.example.domain.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RaceJpaRepo extends JpaRepository<Race, Integer> {

    @Query("SELECT DISTINCT r FROM Race r LEFT JOIN FETCH r.cars rc LEFT JOIN FETCH rc.car WHERE r.id = :id")

    Optional<Race> findByIdWithDetails(@Param("id") int id);
}
