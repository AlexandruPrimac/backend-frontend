package org.example.service;

import org.example.domain.*;
import org.example.exception.CustomApplicationException;
import org.example.repository.CarJpaRepo;
import org.example.repository.CarOwnerShipJpaRepo;
import org.example.repository.RaceJpaRepo;
import org.example.repository.UserJpaRepo;
import org.example.service.Interfaces.CarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CarServiceTest {

    @Autowired
    private CarService sut;

    @Autowired
    private RaceJpaRepo raceRepository;

    @Autowired
    private CarJpaRepo carRepository;

    @Autowired
    private UserJpaRepo userRepository;

    @Autowired
    private CarOwnerShipJpaRepo carOwnerRepository;

    private Race race;
    private Car car;

    @BeforeEach
    void setUp() {

        race = new Race();
        race.setName("F1 Miami GP");
        race.setDate(LocalDate.of(2025, 5, 4));
        race.setTrack("Miami GP");
        race.setLocation("USA");
        race.setDistance(5.41);
        raceRepository.save(race);

        car = new Car();
        car.setBrand("Toyota");
        car.setModel("GR Yaris");
        car.setEngine(1.5);
        car.setHorsepower(300);
        car.setYear(2022);
        car.setCategory(CarCategory.RALLY);
        carRepository.save(car);
    }

    @AfterEach
    void tearDown() {
        if (car != null) {
            carRepository.delete(car);
        }
        if (race != null) {
            raceRepository.delete(race);
        }
    }


    @Test
    void shouldNotAddRaceToNonExistentCar() {
        /// Act
        final Executable action = () -> sut.addRaceToCar(999, race.getId());

        /// Assert
        assertThrows(CustomApplicationException.class, action, "Expected exception for non-existent car");
    }

    @Test
    void shouldNotAddNonExistentRaceToCar() {
        /// Act
        final Executable action = () -> sut.addRaceToCar(car.getId(), 999);

        /// Assert
        assertThrows(CustomApplicationException.class, action, "Expected exception for non-existent race");
    }

    @Test
    void shouldAddCarAndLinkToUser() {
        /// Arrange
        ApplicationUser user = new ApplicationUser();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test@gmail.com");
        user.setPassword("test");
        userRepository.save(user);

        /// Act
        Car createdCar = sut.add("BMW", "M3", 3.0, 473, 2023, CarCategory.SPORTS, user.getId());

        /// Assert
        assertNotNull(createdCar.getId());
        List<CarOwnership> ownerships = carOwnerRepository.findAll();
        assertFalse(ownerships.isEmpty());
        assertEquals(user.getId(), ownerships.get(0).getUser().getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"cITroEn"})
    void shouldSearchIssues(final String query) {
        /// Arrange
        Car car1 = new Car();
        car1.setBrand("Citroen");
        car1.setModel("DS4");
        car1.setEngine(1.6);
        car1.setHorsepower(200);
        car1.setYear(2011);
        car1.setCategory(CarCategory.SPORTS);
        carRepository.save(car1);

        /// Act
        final List<Car> cars = sut.filterCarsDynamically(query);

        /// Assert
        assertEquals(1, cars.size());
        assertEquals("Citroen", cars.get(0).getBrand());
    }

    @Test
    void shouldNotRemoveNonExistentCar() {
        /// Act
        final Executable action = () -> sut.deleteCar(999);

        /// Assert
        assertThrows(CustomApplicationException.class, action);
    }

    @Test
    void shouldRemoveCar() {
        /// Arrange
        Car car = new Car();
        car.setBrand("Renault");
        car.setModel("Megane RS Troph");
        car.setEngine(1.8);
        car.setHorsepower(300);
        car.setYear(2018);
        car.setCategory(CarCategory.SPORTS);
        carRepository.save(car);

        /// Act
        sut.deleteCar(car.getId());

        /// Assert
        assertThrows(NotFoundException.class, () -> sut.getCarById(car.getId()));
    }

    @Test
    void shouldPatchCarYear() {
        /// Arrange
        Car car = new Car();
        car.setBrand("Audi");
        car.setModel("R8");
        car.setEngine(5.2);
        car.setHorsepower(520);
        car.setYear(2014);
        car.setCategory(CarCategory.SPORTS);
        carRepository.save(car);

        /// Act
        final Car patchedCar = sut.patch(car.getId(), "Audi", "R8", 5.2, 520,2015,CarCategory.SPORTS);

        /// Assert
        assertEquals(2015, patchedCar.getYear());
    }
}
