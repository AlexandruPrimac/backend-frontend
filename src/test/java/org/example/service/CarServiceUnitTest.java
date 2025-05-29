package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.domain.*;
import org.example.exception.CustomApplicationException;
import org.example.repository.*;
import org.example.service.Interfaces.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarServiceUnitTest {

    private CarService sut;
    private CarJpaRepo carRepository;
    private RaceJpaRepo raceRepository;
    private CarRacesJpaRepo carRacesRepository;
    private SponsorJpaRepo sponsorJpaRepo;
    private CarSponsorsJpaRepo carSponsorsRepository;
    private UserJpaRepo userRepository;
    private CarOwnerShipJpaRepo carOwnerShipRepository;

    @BeforeEach
    void SetUp() {
        carRepository = mock(CarJpaRepo.class);
        raceRepository = mock(RaceJpaRepo.class);
        carRacesRepository = mock(CarRacesJpaRepo.class);
        carSponsorsRepository = mock(CarSponsorsJpaRepo.class);
        userRepository = mock(UserJpaRepo.class);
        carOwnerShipRepository = mock(CarOwnerShipJpaRepo.class);

        sut = new CarServiceClass(carRepository, raceRepository, carRacesRepository, sponsorJpaRepo, carSponsorsRepository, userRepository, carOwnerShipRepository);
    }

    @Test
    void shouldFindAllCars() {
        /// Arrange
        List<Car> cars = List.of(new Car());
        when(carRepository.findAll()).thenReturn(cars);

        /// Act
        List<Car> result = sut.getAllCars();

        /// Assert
        assertEquals(1, result.size());
    }

    @Test
    void shouldFindCar() {
        /// Arrange
        final Car car = new Car();
        when(carRepository.findByIdWithRaces(1)).thenReturn(Optional.of(car));

        /// Act
        final Car result = sut.getCarById(1); // Same ID

        /// Assert
        assertEquals(car, result);
    }

    @Test
    void shouldNotFindCarThatDoesNotExists() {
        /// Arrange
        final Car car = new Car();
        when(carRepository.findByIdWithRaces(1)).thenReturn(Optional.of(car));

        /// Act
        final Executable action = () -> sut.getCarById(2); // Different ID

        /// Assert
        assertThrows(NotFoundException.class, action);
    }

    @Test
    void shouldAddCar() {
        /// Arrange
        Car input = new Car();
        ApplicationUser user = new ApplicationUser();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> {
            Car car = invocation.getArgument(0);
            car.setId(1);
            return car;
        });

        /// Act
        Car saved = sut.add("Audi", "R8", 5.2, 510, 2012, CarCategory.SPORTS, 1);

        /// Assert
        assertEquals("Audi", saved.getBrand());
        verify(userRepository).findById(1);
        verify(carRepository).save(any(Car.class));
        verify(carOwnerShipRepository).save(any(CarOwnership.class));
    }

    @Test
    void shouldReturnCarsIfFilteredByBrand() {
        /// Arrange
        Car car1 = new Car();
        car1.setBrand("Mercedes");
        car1.setModel("SL500");
        car1.setEngine(5.4);
        car1.setHorsepower(367);
        car1.setYear(2008);
        car1.setCategory(CarCategory.SPORTS);

        Car car2 = new Car();
        car2.setBrand("Mercedes");
        car2.setModel("G63 AMG");
        car2.setEngine(4.0);
        car2.setHorsepower(550);
        car2.setYear(2020);
        car2.setCategory(CarCategory.SPORTS);

        List<Car> mockCars = List.of(car1, car2);
        when(carRepository.findCarByBrand("Mercedes")).thenReturn(mockCars);

        /// Act
        List<Car> result = sut.filterCarsDynamically("Mercedes");

        /// Assert
        assertEquals(2, result.size());
        verify(carRepository).findCarByBrand("Mercedes");
    }

    @Test
    void shouldReturnEmptyListWhenNoCarsFound() {
        /// Arrange
        when(carRepository.findCarByBrand("Tesla")).thenReturn(List.of());

        /// Act
        List<Car> result = sut.filterCarsDynamically("Tesla");

        /// Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRemoveCar() {
        /// Arrange
        int carId = 1;
        Car car = new Car();
        car.setId(carId);

        when(carRepository.existsById(carId)).thenReturn(true);
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        /// Act
        sut.deleteCar(carId);

        /// Assert
        verify(carRacesRepository).deleteByCar(car);
        verify(carSponsorsRepository).deleteByCar(car);
        verify(carOwnerShipRepository).deleteByCar(car);
        verify(carRepository).delete(car);
    }

    @Test
    void shouldThrowExceptionIfCarDoesNotExist() {
        /// Arrange
        int carId = 2;
        when(carRepository.existsById(carId)).thenReturn(false);

        /// Act & Assert
        CustomApplicationException ex = assertThrows(
                CustomApplicationException.class,
                () -> sut.deleteCar(carId)
        );

        assertEquals("Car not found with ID: " + carId, ex.getMessage());

        verify(carRepository, never()).delete(any());
    }

    @Test
    void shouldFindRacesWhenCarExists() {
        /// Arrange
        int carId = 1;

        Race race1 = new Race();
        race1.setName("Race 1");

        Race race2 = new Race();
        race2.setName("Race 2");

        CarRaces carRace1 = new CarRaces();
        carRace1.setRace(race1);

        CarRaces carRace2 = new CarRaces();
        carRace2.setRace(race2);

        Car car = new Car();
        car.setId(carId);
        car.setRaces(List.of(carRace1, carRace2));

        when(carRepository.findByIdWithRaces(carId)).thenReturn(Optional.of(car));

        /// Act
        List<Race> result = sut.getRacesByCarId(carId);

        /// Assert
        assertEquals(2, result.size());
        assertEquals("Race 1", result.get(0).getName());
        assertEquals("Race 2", result.get(1).getName());
        verify(carRepository).findByIdWithRaces(carId);
    }

    @Test
    void shouldReturnEmptyListWhenCarDoesNotExist() {
        /// Arrange
        int carId = 999;
        when(carRepository.findByIdWithRaces(carId)).thenReturn(Optional.empty());

        /// Act
        List<Race> result = sut.getRacesByCarId(carId);

        /// Assert
        assertEquals(0, result.size());
        verify(carRepository).findByIdWithRaces(carId);
    }

    @Test
    void shouldReturnSponsorsWhenCarExists() {
        /// Arrange
        int carId = 1;

        Sponsor sponsor1 = new Sponsor();
        sponsor1.setName("Pirelli");

        Sponsor sponsor2 = new Sponsor();
        sponsor2.setName("Mobil 1");

        CarSponsors cs1 = new CarSponsors();
        cs1.setSponsor(sponsor1);

        CarSponsors cs2 = new CarSponsors();
        cs2.setSponsor(sponsor2);

        Car car = new Car();
        car.setId(carId);
        car.setSponsors(List.of(cs1, cs2));

        when(carRepository.findByIdWithSponsors(carId)).thenReturn(Optional.of(car));

        /// Act
        List<Sponsor> result = sut.getSponsorsByCarId(carId);

        /// Assert
        assertEquals(2, result.size());
        assertEquals("Pirelli", result.get(0).getName());
        assertEquals("Mobil 1", result.get(1).getName());
        verify(carRepository).findByIdWithSponsors(carId);
    }

    @Test
    void shouldReturnEmptyListWhenCarNotFound() {
        /// Arrange
        int carId = 999;
        when(carRepository.findByIdWithSponsors(carId)).thenReturn(Optional.empty());

        /// Act
        List<Sponsor> result = sut.getSponsorsByCarId(carId);

        /// Assert
        assertEquals(0, result.size());
        verify(carRepository).findByIdWithSponsors(carId);
    }

    @Test
    void shouldPatchAllFieldsOfCar() {
        /// Arrange
        Car car = new Car();
        car.setId(1);
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        /// Act
        Car result = sut.patch(1, "BMW", "M3", 3.0, 450, 2022, CarCategory.SPORTS);

        /// Assert
        assertEquals("BMW", result.getBrand());
        assertEquals("M3", result.getModel());
        assertEquals(3.0, result.getEngine());
        assertEquals(450, result.getHorsepower());
        assertEquals(2022, result.getYear());
        assertEquals(CarCategory.SPORTS, result.getCategory());
        verify(carRepository).save(car);
    }

    @Test
    void shouldPatchOnlySomeFieldsOfCar() {
        /// Arrange
        Car existingCar = new Car();
        existingCar.setId(1);
        existingCar.setBrand("OldBrand");
        existingCar.setModel("OldModel");
        existingCar.setEngine(1.0);
        existingCar.setHorsepower(100);
        existingCar.setYear(2000);
        existingCar.setCategory(CarCategory.DRAG);

        when(carRepository.findById(1)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        /// Act: Only brand and year are updated
        Car updatedCar = sut.patch(1, "UpdatedBrand", null, 0, 0, 2023, null);

        /// Assert
        assertEquals("UpdatedBrand", updatedCar.getBrand());  // updated
        assertEquals("OldModel", updatedCar.getModel());      // unchanged
        assertEquals(1.0, updatedCar.getEngine());            // unchanged
        assertEquals(100, updatedCar.getHorsepower());        // unchanged
        assertEquals(2023, updatedCar.getYear());             // updated
        assertEquals(CarCategory.DRAG, updatedCar.getCategory()); // unchanged
        verify(carRepository).save(existingCar);
    }

    @Test
    void shouldThrowWhenCarNotFound() {
        /// Arrange
        when(carRepository.findById(999)).thenReturn(Optional.empty());

        /// Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> sut.patch(999, "Brand", "Model", 1.0, 100, 2023, CarCategory.SPORTS));
    }

}
