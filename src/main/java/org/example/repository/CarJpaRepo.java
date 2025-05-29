package org.example.repository;

import org.example.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarJpaRepo extends JpaRepository<Car, Integer> {

    @Query("SELECT DISTINCT c FROM Car c LEFT JOIN FETCH c.races cr LEFT JOIN FETCH cr.race WHERE c.id = :id")
    Optional<Car> findByIdWithRaces(@Param("id") int id);

    @Query("SELECT DISTINCT c FROM Car c LEFT JOIN FETCH c.sponsors cs LEFT JOIN FETCH cs.sponsor WHERE c.id = :id")
    Optional<Car> findByIdWithSponsors(@Param("id") int id);

    @Query("SELECT c FROM Car c WHERE LOWER(c.brand) LIKE LOWER(:brand)")
    List<Car> findCarByBrand(String brand);
}
