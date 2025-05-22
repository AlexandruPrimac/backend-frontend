package org.example.repository;

import org.example.domain.Car;
import org.example.domain.CarSponsors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarSponsorsJpaRepo extends JpaRepository<CarSponsors, Integer> {
    void deleteByCar(Car car);
}
