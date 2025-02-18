package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.domain.*;
import org.example.exception.CustomApplicationException;
import org.example.presentation.CarViewModel;
import org.example.repository.CarJpaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarServiceClass implements org.example.service.Interfaces.CarService {

    private final static Logger logger = LoggerFactory.getLogger(CarServiceClass.class);

    private final CarJpaRepo carRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CarServiceClass(CarJpaRepo carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getAllCars() {
        List<Car> cars = carRepository.findAll();
        logger.info("Found {} cars", cars.size());
        return cars;
    }

    @Override
    public Car addCar(CarViewModel carViewModel) {
        Car car = new Car(
                carViewModel.getBrand(),
                carViewModel.getModel(),
                carViewModel.getEngine(),
                carViewModel.getHorsepower(),
                carViewModel.getYear(),
                carViewModel.getCategory(),
                carViewModel.getImage()
        );
        logger.info("Adding car: {}", car);
        entityManager.persist(car);
        return car;
    }


    @Override
    public List<Car> filterCarsDynamically(String brand) {
        return carRepository.findCarByBrand(brand);
    }


    @Override
    public Car getCarById(int id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            logger.info("Found car with ID: {}", id);
            return car.get();
        } else {
            logger.warn("Car with ID: {} not found", id);
            return null;
        }
    }

    @Override
    public void deleteCar(int id) {
        // Check if the car exists
        if (!carRepository.existsById(id)) {
            throw new CustomApplicationException("Car not found with ID: " + id);
        }

        // Proceed with deletion if the car exists
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CustomApplicationException("Car not found with ID: " + id));

        // Remove associated relationships (races and sponsors)
        entityManager.createQuery("DELETE FROM CarRaces cr WHERE cr.car = :car")
                .setParameter("car", car)
                .executeUpdate();

        entityManager.createQuery("DELETE FROM CarSponsors cs WHERE cs.car = :car")
                .setParameter("car", car)
                .executeUpdate();


        // Finally, delete the car
        carRepository.delete(car);
    }


    @Override
    public List<Race> getRacesByCarId(int carId) {
        Optional<Car> car = carRepository.findByIdWithRaces(carId);
        if (car.isPresent()) {
            car.get().getRaces().size();
            List<Race> races = car.get().getRaces().stream().map(CarRaces::getRace).toList();
            logger.info("Found {} races for car ID: {}", races.size(), carId);
            return races;
        } else {
            logger.warn("Car with ID: {} not found", carId);
            return List.of();
        }
    }

    @Override
    public List<Sponsor> getSponsorsByCarId(int carId) {
        return carRepository.findByIdWithSponsors(carId)
                .map(car -> car.getSponsors().stream()
                        .map(CarSponsors::getSponsor)
                        .map(sponsor -> {
                            // Force initialization of sponsor data
                            sponsor.getName();  // or any other property access
                            return sponsor;
                        })
                        .toList())
                .orElse(List.of());
    }

}
