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
    CarJpaRepo carRepository;

    @Autowired
    private RaceJpaRepo sut;

    @AfterEach
    void tearDown() {
        carRacesRepository.deleteAll();
        sut.deleteAll();
        carRepository.deleteAll();
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

        /// Act
        Executable action = () -> sut.save(race);

        /// Assert
        assertThrows(ConstraintViolationException.class, action);
    }

    @Test
    void shouldHandleMultipleCarsForOneRace() {
        /// Arrange
        Race race = new Race();
        race.setName("Monaco GP");
        race.setDate(LocalDate.of(2024, 5, 1));
        race.setTrack("Monaco Circuit");
        race.setLocation("Monaco");
        race.setDistance(305.0);
        sut.save(race);

        // Create multiple cars
        Car car1 = new Car();
        car1.setBrand("Ferrari");
        car1.setModel("SF-25");
        car1.setEngine(1.6);
        car1.setHorsepower(1000);
        car1.setYear(2025);
        car1.setCategory(CarCategory.F1);
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setBrand("Mercedes");
        car2.setModel("W12");
        car2.setEngine(1.6);
        car2.setHorsepower(1000);
        car2.setYear(2025);
        car2.setCategory(CarCategory.F1);
        carRepository.save(car2);

        Car car3 = new Car();
        car3.setBrand("Red Bull");
        car3.setModel("RB16");
        car3.setEngine(1.6);
        car3.setHorsepower(1000);
        car3.setYear(2025);
        car3.setCategory(CarCategory.F1);
        carRepository.save(car3);

        // Create CarRaces relationships
        CarRaces carRace1 = new CarRaces();
        carRace1.setCar(car1);
        carRace1.setRace(race);

        CarRaces carRace2 = new CarRaces();
        carRace2.setCar(car2);
        carRace2.setRace(race);

        CarRaces carRace3 = new CarRaces();
        carRace3.setCar(car3);
        carRace3.setRace(race);

        carRacesRepository.saveAll(List.of(carRace1, carRace2, carRace3)); // Save the car-race relationships

        /// Act
        Race foundRace = sut.findByIdWithCars(race.getId()).orElseThrow();

        /// Assert
        assertEquals(3, foundRace.getCars().size()); // Ensure there are 3 CarRaces associated with this race
    }
}
