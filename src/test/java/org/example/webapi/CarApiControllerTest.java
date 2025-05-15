package org.example.webapi;

import jakarta.persistence.EntityNotFoundException;
import org.example.TestHelper;
import org.example.domain.Car;
import org.example.exception.CustomApplicationException;
import org.example.service.Interfaces.AuthorizationService;
import org.example.service.Interfaces.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.example.TestHelper.ADMIN_EMAIL;
import static org.example.TestHelper.NORMAL_EMAIL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CarApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestHelper testHelper;

    @MockBean
    private CarService carService;

    @MockBean
    private AuthorizationService authorizationService;

    private Car testCar;

    @BeforeEach
    void setUp() {
        testHelper.cleanUp();
        testHelper.createAdmin();
        testHelper.createNormalUser();
        testCar = testHelper.createCar();
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldSearchCars() throws Exception {
        /// Arrange
        Car car1 = testHelper.createCar();
        car1.setBrand("Ferrari");
        Car car2 = testHelper.createCar();
        car2.setBrand("Toyota");

        when(carService.filterCarsDynamically("ferrari")).thenReturn(List.of(car1));

        /// Act
        final var response = mockMvc.perform(get("/api/cars?brand=ferrari"));

        /// Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].brand", equalTo("Ferrari")))
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAddCar() throws Exception {
        /// Arrange
        final var request = post("/api/cars")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "brand": "Audi",
                            "model": "RS3",
                            "engine": 2.5,
                            "horsePower": 400,
                            "year": 2023,
                            "category": "SPORTS"
                        }
                        """);

        when(carService.add(any(), any(), any(), anyInt(), anyInt(), any(), anyInt()))
                .thenReturn(testCar);

        /// Act
        final var response = mockMvc.perform(request);

        /// Assert
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCar.getId()))
                .andExpect(jsonPath("$.brand").value(testCar.getBrand()))
                .andExpect(jsonPath("$.model").value(testCar.getModel()));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldUpdateCar() throws Exception {
        /// Arrange
        when(carService.patch(
                anyInt(),  // id
                any(),     // brand
                any(),     // model
                anyDouble(), // engine
                anyInt(),  // horsepower
                anyInt(),  // year
                any()      // category
        )).thenReturn(testCar);

        final var request = patch("/api/cars/{id}", testCar.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "brand": "Audi",
                            "model": "RS3",
                            "engine": 2.5,
                            "horsepower": 400,
                            "year": 2023,
                            "category": "SPORTS"
                        }
                        """);

        /// Act
        final var response = mockMvc.perform(request);

        /// Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCar.getId()))
                .andExpect(jsonPath("$.brand").value(testCar.getBrand()));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldReturn404WhenUpdatingNonExistentCar() throws Exception {
        /// Arrange
        when(carService.patch(anyInt(), any(), any(), anyDouble(), anyInt(), anyInt(), any()))
                .thenThrow(new EntityNotFoundException("Car not found"));

        final var request = patch("/api/cars/{id}", 999)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "brand": "Audi",
                            "model": "RS3",
                            "engine": 2.5,
                            "horsepower": 400,
                            "year": 2023,
                            "category": "SPORTS"
                        }
                        """);

        /// Act
        final var response = mockMvc.perform(request);

        /// Assert
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldReturn400WhenUpdatingWithInvalidData() throws Exception {
        /// Arrange
        when(carService.patch(anyInt(), any(), any(), anyDouble(), anyInt(), anyInt(), any()))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        final var request = patch("/api/cars/{id}", testCar.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "brand": "Audi",
                            "model": "RS3",
                            "engine": -2.5,
                            "horsepower": -400,
                            "year": 2023,
                            "category": "SPORTS"
                        }
                        """);

        /// Act
        final var response = mockMvc.perform(request);

        /// Assert
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldDeleteCar() throws Exception {
        /// Arrange
        final var request = delete("/api/cars/{id}", testCar.getId())
                .with(csrf());

        /// Act
        final var response = mockMvc.perform(request);

        /// Assert
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldReturn404WhenDeletingNonExistentCar() throws Exception {
        /// Arrange
        doThrow(new CustomApplicationException("Car not found"))
                .when(carService).deleteCar(anyInt());

        final var request = delete("/api/cars/{id}", 999)
                .with(csrf());

        /// Act
        final var response = mockMvc.perform(request);

        /// Assert
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldReturn500WhenCarNotFound() throws Exception {
        /// Arrange
        when(carService.addRaceToCar(anyInt(), anyInt()))
                .thenThrow(new CustomApplicationException("Car not found"));

        final var request = patch("/api/cars/{id}/add-race?raceId=1", 999)
                .with(csrf());

        /// Act
        final var response = mockMvc.perform(request);

        /// Assert
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldReturn500WhenRaceNotFound() throws Exception {
        /// Arrange
        when(carService.addRaceToCar(anyInt(), anyInt()))
                .thenThrow(new CustomApplicationException("Race not found"));

        final var request = patch("/api/cars/{id}/add-race?raceId=999", testCar.getId())
                .with(csrf());

        /// Act
        final var response = mockMvc.perform(request);

        /// Assert
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }


    // Authorization tests for the api

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAllowAdminToUpdateCar() throws Exception {
        /// Arrange
        when(authorizationService.canEditOrDeleteCar(any(), any())).thenReturn(true);
        when(carService.patch(anyInt(), any(), any(), anyDouble(), anyInt(), anyInt(), any()))
                .thenReturn(testCar);

        /// Act & Assert
        mockMvc.perform(patch("/api/cars/{id}", testCar.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "brand": "Audi",
                                    "model": "RS3",
                                    "engine": 2.5,
                                    "horsepower": 400,
                                    "year": 2023,
                                    "category": "SPORTS"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAllowAdminToDeleteCar() throws Exception {
        /// Arrange
        when(authorizationService.canEditOrDeleteCar(any(), any())).thenReturn(true);

        /// Act & Assert
        mockMvc.perform(delete("/api/cars/{id}", testCar.getId())
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = NORMAL_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAllowOwnerToUpdateCar() throws Exception {
        /// Arrange
        when(authorizationService.canEditOrDeleteCar(any(), any())).thenReturn(true);
        when(carService.patch(anyInt(), any(), any(), anyDouble(), anyInt(), anyInt(), any()))
                .thenReturn(testCar);

        /// Act & Assert
        mockMvc.perform(patch("/api/cars/{id}", testCar.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "brand": "Audi",
                                    "model": "RS3",
                                    "engine": 2.5,
                                    "horsepower": 400,
                                    "year": 2023,
                                    "category": "SPORTS"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = NORMAL_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAllowOwnerToDeleteCar() throws Exception {
        /// Arrange
        when(authorizationService.canEditOrDeleteCar(any(), any())).thenReturn(true);

        /// Act & Assert
        mockMvc.perform(delete("/api/cars/{id}", testCar.getId())
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotAllowAnonymousUserToUpdateCar() throws Exception {
        /// Act & Assert
        mockMvc.perform(patch("/api/cars/{id}", testCar.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "brand": "Audi",
                                    "model": "RS3",
                                    "engine": 2.5,
                                    "horsepower": 400,
                                    "year": 2023,
                                    "category": "SPORTS"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldNotAllowAnonymousUserToDeleteCar() throws Exception {
        /// Act & Assert
        mockMvc.perform(delete("/api/cars/{id}", testCar.getId())
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}