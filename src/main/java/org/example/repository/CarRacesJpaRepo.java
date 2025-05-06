package org.example.repository;

import org.example.domain.CarRaces;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRacesJpaRepo extends JpaRepository<CarRaces, Integer> {

}
