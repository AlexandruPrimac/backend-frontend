package org.example.webapi;

import jakarta.persistence.EntityNotFoundException;
import org.example.domain.Car;
import org.example.domain.CarCategory;
import org.example.exception.CustomApplicationException;
import org.example.security.CustomUserDetails;
import org.example.service.Interfaces.CarService;
import org.example.webapi.dto.request.AddCarDto;
import org.example.webapi.dto.request.PatchCarDto;
import org.example.webapi.dto.response.CarDto;
import org.example.webapi.dto.response.CarMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CarApiControllerUnitTest {
    @Autowired
    private CarApiController sut;

    @MockBean
    private CarService carService;

    @MockBean
    private CarMapper carMapper;

    @Test
    void shouldAddCar() {
        /// Arrange
        final AddCarDto dto = new AddCarDto("test", "test", 2.5, 300, 2023, CarCategory.SPORTS);

        final CustomUserDetails user = new CustomUserDetails(1, "test@example.com", "Test", "User", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        final Car expectedCar = new Car();
        expectedCar.setId(1);
        expectedCar.setBrand("test");
        expectedCar.setModel("test");
        expectedCar.setEngine(2.5);
        expectedCar.setHorsepower(300);
        expectedCar.setYear(2023);
        expectedCar.setCategory(CarCategory.SPORTS);

        final CarDto expectedCarDto = new CarDto(1, "test", "test", "image.jpg");

        when(carService.add("test", "test", 2.5, 300, 2023, CarCategory.SPORTS, 1)).thenReturn(expectedCar);

        when(carMapper.toCarDto(expectedCar)).thenReturn(expectedCarDto);

        /// Act
        final var response = sut.addCar(dto, user);

        /// Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedCarDto.id(), response.getBody().id());
        assertEquals(expectedCarDto.brand(), response.getBody().brand());
        assertEquals(expectedCarDto.model(), response.getBody().model());
        assertEquals(expectedCarDto.image(), response.getBody().image());
    }

    @Test
    void shouldFilterCarsByBrand() {
        /// Arrange
        final String brand = "test";
        final List<Car> cars = Arrays.asList(createTestCar(1), createTestCar(2));
        final List<CarDto> expectedDtos = Arrays.asList(new CarDto(1, "test", "test", "image1.jpg"), new CarDto(2, "test", "test", "image2.jpg"));

        when(carService.filterCarsDynamically(brand)).thenReturn(cars);
        when(carMapper.toCarDto(cars.get(0))).thenReturn(expectedDtos.get(0));
        when(carMapper.toCarDto(cars.get(1))).thenReturn(expectedDtos.get(1));

        /// Act
        final ResponseEntity<List<CarDto>> response = sut.filter(brand);

        /// Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedDtos, response.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoCarsFound() {
        /// Arrange
        final String brand = "NonExistentBrand";
        when(carService.filterCarsDynamically(brand)).thenReturn(Collections.emptyList());

        /// Act
        final ResponseEntity<List<CarDto>> response = sut.filter(brand);

        /// Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void shouldRemoveCarSuccessfully() {
        /// Arrange
        final int carId = 1;
        final CustomUserDetails user = createTestUser();

        /// Act
        final ResponseEntity<Void> response = sut.remove(carId, user);

        /// Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(carService).deleteCar(carId);
    }

    @Test
    void shouldReturnNotFoundWhenRemovingNonExistentCar() {
        /// Arrange
        final int carId = 999;
        final CustomUserDetails user = createTestUser();
        doThrow(new CustomApplicationException("Car not found"))
                .when(carService).deleteCar(carId);

        /// Act
        final ResponseEntity<Void> response = sut.remove(carId, user);

        /// Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldUpdateCarSuccessfully() {
        /// Arrange
        final int carId = 1;
        final PatchCarDto patchDto = new PatchCarDto(carId, "test", "test", 2.5, 300, 2023, CarCategory.SPORTS);

        final Car updatedCar = createTestCar(carId);
        final CarDto expectedDto = new CarDto(carId, "test", "test", "test.jpg");

        when(carService.patch(carId, patchDto.brand(), patchDto.model(), patchDto.engine(), patchDto.horsepower(), patchDto.year(), patchDto.category())).thenReturn(updatedCar);
        when(carMapper.toCarDto(updatedCar)).thenReturn(expectedDto);

        /// Act
        final ResponseEntity<CarDto> response = sut.updateCar(carId, patchDto);

        /// Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedDto, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentCar() {
        /// Arrange
        final int carId = 999;
        final PatchCarDto patchDto = new PatchCarDto(carId, "test", "test", 2.5, 300, 2023, CarCategory.SPORTS);

        when(carService.patch(anyInt(), anyString(), anyString(), anyDouble(), anyInt(), anyInt(), any(CarCategory.class))).thenThrow(new EntityNotFoundException("Car not found"));

        /// Act
        final ResponseEntity<CarDto> response = sut.updateCar(carId, patchDto);

        /// Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenUpdateFails() {
        /// Arrange
        final int carId = 1;
        final PatchCarDto patchDto = new PatchCarDto(carId, "test", "test", 2.5, 300, 2023, CarCategory.SPORTS);

        when(carService.patch(anyInt(), anyString(), anyString(), anyDouble(), anyInt(), anyInt(), any(CarCategory.class))).thenThrow(new RuntimeException("Update failed"));

        /// Act
        final ResponseEntity<CarDto> response = sut.updateCar(carId, patchDto);

        /// Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Helper Methods for creating car and user
    private Car createTestCar(int id) {
        Car car = new Car();
        car.setId(id);
        car.setBrand("test");
        car.setModel("test");
        car.setEngine(2.5);
        car.setHorsepower(300);
        car.setYear(2023);
        car.setCategory(CarCategory.SPORTS);
        car.setImage("image.jpg");
        return car;
    }

    private CustomUserDetails createTestUser() {
        return new CustomUserDetails(
                1,
                "test@example.com",
                "Test",
                "User",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
