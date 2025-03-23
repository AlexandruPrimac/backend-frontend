package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.domain.*;
import org.example.exception.CustomApplicationException;
import org.example.repository.CarJpaRepo;
import org.example.repository.RaceJpaRepo;
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

    private final RaceJpaRepo raceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CarServiceClass(CarJpaRepo carRepository, RaceJpaRepo raceRepository) {
        this.carRepository = carRepository;
        this.raceRepository = raceRepository;
    }

    @Override
    public List<Car> getAllCars() {
        List<Car> cars = carRepository.findAll();
        logger.info("Found {} cars", cars.size());
        return cars;
    }

    @Override
    public Car add(String brand, String model, Double engineCapacity, int horsepower, int year, CarCategory category) {
        Car car = new Car();
        logger.info("Adding car: {}", car);
        car.setBrand(brand);
        car.setModel(model);
        car.setEngine(engineCapacity);
        car.setHorsepower(horsepower);
        car.setYear(year);
        car.setCategory(category);
        return carRepository.save(car);
    }


    @Override
    public List<Car> filterCarsDynamically(String brand) {
        return carRepository.findCarByBrand(brand);
    }


    @Override
    public Car getCarById(int id) {
        Optional<Car> car = carRepository.findByIdWithRaces(id);
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

    @Override
    public Car patch(int id, String brand, String model, double engine, int horsepower, int year, CarCategory category) {
        Car car = carRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Car not found"));

        if (brand != null) {
            car.setBrand(brand);
        }

        if (model != null) {
            car.setModel(model);
        }

        if (engine != 0) {
            car.setEngine(engine);
        }

        if (horsepower != 0) {
            car.setHorsepower(horsepower);
        }

        if (year != 0) {
            car.setYear(year);
        }

        if (category != null) {
            car.setCategory(category);
        }

        return carRepository.save(car);
    }

    @Override
    public Car addRaceToCar(int carId, int raceId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomApplicationException("Car not found with id " + carId));

        Race race = raceRepository.findById(raceId)
                .orElseThrow(() -> new CustomApplicationException("Race not found with id " + raceId));

        boolean isRaceLinked = car.getRaces().stream()
                .anyMatch(cr -> cr.getRace().getId() == raceId);

        if (!isRaceLinked) {
            CarRaces carRace = new CarRaces();
            carRace.setCar(car);
            carRace.setRace(race);
            car.getRaces().add(carRace);

            entityManager.persist(carRace);
            entityManager.flush();
        }

        return carRepository.save(car);
    }
}
