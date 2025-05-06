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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class RaceRepositoryTest {

    @Autowired
    private CarRacesJpaRepo carRacesRepository;

    @Autowired
    private CarJpaRepo carRepository;

    @Autowired
    private RaceJpaRepo sut;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void shouldNotAllowNegativeDistance() {
        /// Arrange
        Race race = new Race();
        race.setName("MX-5 CUP");
        race.setDate(LocalDate.of(2024, 6, 8));
        race.setTrack("Laguna Seca Circuit");
        race.setLocation("Monaco");
        race.setDistance(-1);

        /// Act + Assert
        assertThrows(ConstraintViolationException.class, () -> sut.save(race));
    }

    @Test
    void shouldHandleMultipleCarsForOneRace() {
        /// Arrange
        Race race = testHelper.createRace();

        Car car1 = testHelper.createCar();

        /// Create variations of car manually or expand helper later
        Car car2 = new Car("Mercedes", "W12", 1.6, 1000, 2025, CarCategory.F1, null);
        Car car3 = new Car("Red Bull", "RB16", 1.6, 1000, 2025, CarCategory.F1, null);
        carRepository.saveAll(List.of(car2, car3));

        /// Create CarRaces relationships
        CarRaces carRace1 = new CarRaces();
        carRace1.setCar(car1);
        carRace1.setRace(race);

        CarRaces carRace2 = new CarRaces();
        carRace2.setCar(car2);
        carRace2.setRace(race);

        CarRaces carRace3 = new CarRaces();
        carRace3.setCar(car3);
        carRace3.setRace(race);

        carRacesRepository.saveAll(List.of(carRace1, carRace2, carRace3));

        /// Act
        Race foundRace = sut.findByIdWithCars(race.getId()).orElseThrow();

        /// Assert
        assertEquals(3, foundRace.getCars().size());
    }
}
