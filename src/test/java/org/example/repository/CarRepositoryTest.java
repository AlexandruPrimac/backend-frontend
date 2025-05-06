package org.example.repository;

import jakarta.validation.ConstraintViolationException;
import org.example.domain.Car;
import org.example.domain.CarCategory;
import org.example.domain.CarRaces;
import org.example.domain.Race;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
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
    private SponsorJpaRepo sponsorRepository;

    @Autowired
    private RaceJpaRepo raceRepository;

    @Autowired
    private CarRacesJpaRepo carRacesRepository;

    @Autowired
    CarJpaRepo sut;


    @AfterEach
    void tearDown() {
        carRacesRepository.deleteAll();
        raceRepository.deleteAll();
        sponsorRepository.deleteAll();
        sut.deleteAll();
    }

    @Test
    void shouldAddCar() {
        /// Arrange
        final Car car = new Car();
        car.setBrand("BMW");
        car.setModel("M4");
        car.setEngine(3.0);
        car.setHorsepower(430);
        car.setYear(2015);
        car.setCategory(CarCategory.SPORTS);

        /// Act
        sut.save(car);

        /// Assert
        assertNotNull(car.getId());
        assertEquals(1, sut.findAll().size());
    }

    @Test
    void shouldDeleteCarAndAlsoDeleteAssociatedRaces() {
        /// Arrange
        Race race = new Race();
        race.setName("Spa GP F1");
        race.setDate(LocalDate.of(2025, 1, 28));
        race.setTrack("Spa Francorchamps");
        race.setLocation("Belgium");
        race.setDistance(7);
        raceRepository.save(race);

        Car car = new Car();
        car.setBrand("Ferrari");
        car.setModel("SF-25");
        car.setEngine(1.6);
        car.setHorsepower(1000);
        car.setYear(2025);
        car.setCategory(CarCategory.F1);
        sut.save(car);

        CarRaces carRace = new CarRaces();
        carRace.setCar(car);
        carRace.setRace(race);
        carRacesRepository.save(carRace);

        /// Act
        sut.deleteById(car.getId());

        /// Assert
        assertFalse(sut.findById(car.getId()).isPresent());

        // CarRaces should be gone (if cascade or manually deleted) I added cascade = {CascadeType.PERSIST}, orphanRemoval = true to the OneToMany rel between cars and races
        assertEquals(0, carRacesRepository.findAll().size());

        // Race should still exist
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

        /// Act
        Executable action = () -> sut.save(car);  // Expect failure for null brand.

        /// Assert
        assertThrows(DataIntegrityViolationException.class, action);
    }

    @Test
    void shouldNotAllowNegativeHorsepower() {
        /// Arrange
        Car car = new Car("Ferrari", "SF-25", 1.6, -100, 2025, CarCategory.F1, null);

        /// Act
        Executable action = () -> sut.save(car);

        /// Assert
        assertThrows(ConstraintViolationException.class, action);
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
        Car car = new Car();
        car.setBrand("Porsche");
        car.setModel("911 GT3");
        car.setEngine(4.0);
        car.setHorsepower(502);
        car.setYear(2022);
        car.setCategory(CarCategory.SPORTS);
        sut.save(car);

        Race race1 = new Race();
        race1.setName("Le Mans");
        race1.setDate(LocalDate.of(2024, 6, 15));
        race1.setTrack("Circuit de la Sarthe");
        race1.setLocation("France");
        race1.setDistance(13.6);

        Race race2 = new Race();
        race2.setName("Nürburgring 24h");
        race2.setDate(LocalDate.of(2024, 7, 1));
        race2.setTrack("Nordschleife");
        race2.setLocation("Germany");
        race2.setDistance(25.4);

        raceRepository.saveAll(List.of(race1, race2));

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
        assertEquals(2, foundCar.getRaces().size());  // Should be 2 races for this car
    }

    @Test
    void shouldLoadRacesEagerly() {
        /// Arrange
        Car car = new Car("Ferrari", "SF-25", 1.6, 1000, 2025, CarCategory.F1, null);
        Race race = new Race("Monaco GP", LocalDate.of(2024, 5, 1), "Monaco Circuit", "Monaco", 305.0, null);
        sut.save(car);
        raceRepository.save(race);

        CarRaces carRace = new CarRaces();
        carRace.setCar(car);
        carRace.setRace(race);
        carRacesRepository.save(carRace);

        /// Act
        Car foundCar = sut.findByIdWithRaces(car.getId()).orElseThrow();

        /// Assert
        assertFalse(foundCar.getRaces().isEmpty()); // check if races are eagerly loaded
        assertEquals("Monaco GP", foundCar.getRaces().get(0).getRace().getName());
    }
}
