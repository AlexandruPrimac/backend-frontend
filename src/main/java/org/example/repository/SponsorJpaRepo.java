package org.example.repository;

import org.example.domain.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SponsorJpaRepo extends JpaRepository<Sponsor, Integer> {

    @Query("SELECT s FROM Sponsor s LEFT JOIN FETCH s.cars WHERE s.id = :id")
    Optional<Sponsor> findByIdWithDetails(@Param("id") int id);

    @Query("SELECT s FROM Sponsor s WHERE LOWER(s.name) LIKE LOWER(:name)")
    List<Sponsor> filterSponsorsByName(String name);
}
