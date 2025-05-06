package org.example.repository;

import jakarta.validation.ConstraintViolationException;
import org.example.TestHelper;
import org.example.domain.Car;
import org.example.domain.CarCategory;
import org.example.domain.CarRaces;
import org.example.domain.Race;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CarRepositoryTest {

    @Autowired
    private CarJpaRepo sut;

    @Autowired
    private RaceJpaRepo raceRepository;

    @Autowired
    private CarRacesJpaRepo carRacesRepository;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void shouldAddCar() {
        /// Arrange
        Car car = testHelper.createCar();

        /// Act + Assert
        assertNotNull(car.getId());
        assertEquals(1, sut.findAll().size());
    }

    @Test
    void shouldDeleteCarAndAlsoDeleteAssociatedRaces() {
        /// Arrange
        Race race = testHelper.createRace();
        Car car = testHelper.createCar();

        CarRaces carRace = new CarRaces();
        carRace.setCar(car);
        carRace.setRace(race);
        carRacesRepository.save(carRace);

        /// Act
        sut.deleteById(car.getId());

        /// Assert
        assertFalse(sut.findById(car.getId()).isPresent());
        assertEquals(0, carRacesRepository.findAll().size()); // assuming orphanRemoval = true
        assertEquals(1, raceRepository.findAll().size());
    }

    @Test
    void shouldNotAllowNullBrand() {
        /// Arrange
        Car car = new Car();
        car.setModel("SF-25");
        car.setEngine(1.6);
        car.setHorsepower(1000);
        car.setYear(2025);
        car.setCategory(CarCategory.F1);

        /// Act + Assert
        assertThrows(DataIntegrityViolationException.class, () -> sut.save(car));
    }

    @Test
    void shouldNotAllowNegativeHorsepower() {
        /// Arrange
        Car car = new Car("Ferrari", "SF-25", 1.6, -100, 2025, CarCategory.F1, null);

        /// Act + Assert
        assertThrows(ConstraintViolationException.class, () -> sut.save(car));
    }

    @Test
    void shouldLoadCarWithNoRaces() {
        /// Arrange
        Car car = new Car("McLaren", "P1", 3.8, 903, 2013, CarCategory.SPORTS, null);
        sut.save(car);

        /// Act
        Car foundCar = sut.findByIdWithRaces(car.getId()).orElseThrow();

        /// Assert
        assertNotNull(foundCar);
        assertTrue(foundCar.getRaces().isEmpty());
    }

    @Test
    void shouldHandleMultipleRacesForOneCar() {
        /// Arrange
        Car car = testHelper.createCar();
        Race race1 = testHelper.createRace(); // reuse once; clone next

        Race race2 = new Race();
        race2.setName("Nürburgring 24h");
        race2.setDate(LocalDate.of(2024, 7, 1));
        race2.setTrack("Nordschleife");
        race2.setLocation("Germany");
        race2.setDistance(25.4);
        raceRepository.save(race2);

        CarRaces carRace1 = new CarRaces();
        carRace1.setCar(car);
        carRace1.setRace(race1);

        CarRaces carRace2 = new CarRaces();
        carRace2.setCar(car);
        carRace2.setRace(race2);

        carRacesRepository.saveAll(List.of(carRace1, carRace2));

        /// Act
        Car foundCar = sut.findByIdWithRaces(car.getId()).orElseThrow();

        /// Assert
        assertEquals(2, foundCar.getRaces().size());
    }

    @Test
    void shouldLoadRacesEagerly() {
        /// Arrange
        Car car = testHelper.createCar();
        Race race = new Race("Monaco GP", LocalDate.of(2024, 5, 1), "Monaco Circuit", "Monaco", 305.0, null);
        raceRepository.save(race);

        CarRaces carRace = new CarRaces();
        carRace.setCar(car);
        carRace.setRace(race);
        carRacesRepository.save(carRace);

        /// Act
        Car foundCar = sut.findByIdWithRaces(car.getId()).orElseThrow();

        /// Assert
        assertFalse(foundCar.getRaces().isEmpty());
        assertEquals("Monaco GP", foundCar.getRaces().get(0).getRace().getName());
    }
}

