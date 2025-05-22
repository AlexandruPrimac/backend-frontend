package org.example.repository;

import org.example.domain.Car;
import org.example.domain.CarRaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CarRacesJpaRepo extends JpaRepository<CarRaces, Integer> {

    void deleteByCar(Car car);
}
