package org.example.repository;

import org.example.domain.Car;
import org.example.domain.CarOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarOwnerShipJpaRepo extends JpaRepository<CarOwnership, Long> {

    Optional<CarOwnership> findByCarIdAndUserId(int carId, int userId);

    void deleteByCar(Car car);
}
