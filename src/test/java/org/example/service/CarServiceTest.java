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
    private ApplicationUser user;

    @BeforeEach
    void setUp() {
        user = new ApplicationUser();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test@gmail.com");
        user.setPassword("test");
        userRepository.save(user);

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
        carOwnerRepository.deleteAll();

        if (car != null) {
            carRepository.delete(car);
        }
        if (race != null) {
            raceRepository.delete(race);
        }
        if (user != null) {
            userRepository.delete(user);
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
        ApplicationUser testUser = new ApplicationUser();
        testUser.setFirstName("Test");
        testUser.setLastName("Test");
        testUser.setEmail("test2@gmail.com");
        testUser.setPassword("testttt");
        userRepository.save(testUser);

        try {
            /// Act
            Car createdCar = sut.add("BMW", "M3", 3.0, 473, 2023, CarCategory.SPORTS, testUser.getId());

            /// Assert
            assertNotNull(createdCar.getId());
            List<CarOwnership> ownerships = carOwnerRepository.findAll();
            assertFalse(ownerships.isEmpty());
            assertEquals(testUser.getId(), ownerships.get(0).getUser().getId());
        } finally {
            carOwnerRepository.deleteAll();
            userRepository.delete(testUser);
        }
    }

    @Test
    void shouldNotAddCarWithInvalidUser() {
        /// Act
        final Executable action = () -> sut.add("BMW", "M3", 3.0, 473, 2023, CarCategory.SPORTS, 999);

        /// Assert
        assertThrows(Exception.class, action, "Expected exception for non-existent user");
    }

    @Test
    void shouldNotAddCarWithInvalidYear() {
        /// Act
        final Executable action = () -> sut.add("BMW", "M3", 3.0, 473, 1884, CarCategory.SPORTS, user.getId());

        /// Assert
        assertThrows(Exception.class, action, "Expected exception for invalid year");
    }

    @Test
    void shouldNotAddCarWithInvalidHorsepower() {
        /// Act
        final Executable action = () -> sut.add("BMW", "M3", 3.0, -1, 2023, CarCategory.SPORTS, user.getId());

        /// Assert
        assertThrows(Exception.class, action, "Expected exception for negative horsepower");
    }

    @Test
    void shouldNotAddCarWithNullBrand() {
        /// Act
        final Executable action = () -> sut.add(null, "M3", 3.0, 473, 2023, CarCategory.SPORTS, user.getId());

        /// Assert
        assertThrows(Exception.class, action, "Expected exception for null brand");
    }

    @Test
    void shouldNotAddCarWithNullModel() {
        /// Act
        final Executable action = () -> sut.add("BMW", null, 3.0, 473, 2023, CarCategory.SPORTS, user.getId());

        /// Assert
        assertThrows(Exception.class, action, "Expected exception for null model");
    }

    @ParameterizedTest
    @ValueSource(strings = {"cITroEn", "CITROEN", "citroen"})
    void shouldSearchIssuesCaseInsensitive(final String query) {
        /// Arrange
        Car car1 = new Car();
        car1.setBrand("Citroen");
        car1.setModel("DS4");
        car1.setEngine(1.6);
        car1.setHorsepower(200);
        car1.setYear(2011);
        car1.setCategory(CarCategory.SPORTS);
        carRepository.save(car1);

        try {
            /// Act
            final List<Car> cars = sut.filterCarsDynamically(query);

            /// Assert
            assertEquals(1, cars.size());
            assertEquals("Citroen", cars.get(0).getBrand());
        } finally {
            carRepository.delete(car1);
        }
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
        Car testCar = new Car();
        testCar.setBrand("Renault");
        testCar.setModel("Megane RS Troph");
        testCar.setEngine(1.8);
        testCar.setHorsepower(300);
        testCar.setYear(2018);
        testCar.setCategory(CarCategory.SPORTS);
        carRepository.save(testCar);

        try {
            /// Act
            sut.deleteCar(testCar.getId());

            /// Assert
            assertThrows(NotFoundException.class, () -> sut.getCarById(testCar.getId()));
        } finally {
            carRepository.delete(testCar);
        }
    }

    @Test
    void shouldPatchCarYear() {
        /// Arrange
        Car testCar = new Car();
        testCar.setBrand("Audi");
        testCar.setModel("R8");
        testCar.setEngine(5.2);
        testCar.setHorsepower(520);
        testCar.setYear(2014);
        testCar.setCategory(CarCategory.SPORTS);
        carRepository.save(testCar);

        try {
            /// Act
            final Car patchedCar = sut.patch(testCar.getId(), "Audi", "R8", 5.2, 520, 2015, CarCategory.SPORTS);

            /// Assert
            assertEquals(2015, patchedCar.getYear());
        } finally {
            carRepository.delete(testCar);
        }
    }

    @Test
    void shouldNotPatchNonExistentCar() {
        /// Act
        final Executable action = () -> sut.patch(999, "Audi", "R8", 5.2, 520, 2015, CarCategory.SPORTS);

        /// Assert
        assertThrows(Exception.class, action, "Expected exception for non-existent car");
    }

    @Test
    void shouldNotPatchCarWithInvalidYear() {
        /// Arrange
        Car testCar = new Car();
        testCar.setBrand("Audi");
        testCar.setModel("R8");
        testCar.setEngine(5.2);
        testCar.setHorsepower(520);
        testCar.setYear(2014);
        testCar.setCategory(CarCategory.SPORTS);
        carRepository.save(testCar);

        try {
            /// Act
            final Executable action = () -> sut.patch(testCar.getId(), "Audi", "R8", 5.2, 520, 1884, CarCategory.SPORTS);

            /// Assert
            assertThrows(Exception.class, action, "Expected exception for invalid year");
        } finally {
            carRepository.delete(testCar);
        }
    }

    @Test
    void shouldNotPatchCarWithInvalidHorsepower() {
        /// Arrange
        Car testCar = new Car();
        testCar.setBrand("Audi");
        testCar.setModel("R8");
        testCar.setEngine(5.2);
        testCar.setHorsepower(520);
        testCar.setYear(2014);
        testCar.setCategory(CarCategory.SPORTS);
        carRepository.save(testCar);

        try {
            /// Act
            final Executable action = () -> sut.patch(testCar.getId(), "Audi", "R8", 5.2, -1, 2015, CarCategory.SPORTS);

            /// Assert
            assertThrows(Exception.class, action, "Expected exception for negative horsepower");
        } finally {
            carRepository.delete(testCar);
        }
    }
}
