package org.example.repository;

import org.example.domain.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RaceJpaRepo extends JpaRepository<Race, Integer> {

    @Query("SELECT DISTINCT r FROM Race r LEFT JOIN FETCH r.cars rc LEFT JOIN FETCH rc.car WHERE r.id = :id")
    Optional<Race> findByIdWithDetails(@Param("id") int id);

    @Query("SELECT r FROM Race r WHERE LOWER(r.location) LIKE LOWER(:location)")
    List<Race> filterRacesByLocation(String location);

    @Query("SELECT DISTINCT r FROM Race r LEFT JOIN FETCH r.cars rc LEFT JOIN FETCH rc.car WHERE r.id = :id")
    Optional<Race> findByIdWithCars(@Param("id") int id);
}
