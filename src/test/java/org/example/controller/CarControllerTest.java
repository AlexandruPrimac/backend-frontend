package org.example.controller;

import org.example.TestHelper;
import org.example.domain.Car;
import org.example.domain.Race;
import org.example.domain.Sponsor;
import org.example.service.Interfaces.AuthorizationService;
import org.example.service.Interfaces.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.example.TestHelper.ADMIN_EMAIL;
import static org.example.TestHelper.NORMAL_EMAIL;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestHelper testHelper;

    @MockBean
    private CarService carService;

    @MockBean
    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        testHelper.cleanUp();
        testHelper.createNormalUser();
        testHelper.createAdmin();
    }

    @Test
    void shouldListAllCars() throws Exception {
        /// Arrange
        List<Car> cars = Arrays.asList(
                testHelper.createCar(),
                testHelper.createCar()
        );
        when(carService.getAllCars()).thenReturn(cars);

        /// Act & Assert
        mockMvc.perform(get("/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("cars"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", cars));
    }

    @Test
    void shouldHandleEmptyCarList() throws Exception {
        /// Arrange
        when(carService.getAllCars()).thenReturn(List.of());

        /// Act & Assert
        mockMvc.perform(get("/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("cars"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", hasSize(0)));
    }

    @Test
    void shouldHandleNullCarList() throws Exception {
        /// Arrange
        when(carService.getAllCars()).thenReturn(null);

        /// Act & Assert
        mockMvc.perform(get("/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("generalError"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAllowAdminToAddCar() throws Exception {
        /// Arrange
        final var request = post("/cars/add")
                .with(csrf())
                .param("brand", "Audi")
                .param("model", "RS3")
                .param("engine", "2.5")
                .param("horsepower", "400")
                .param("year", "2023")
                .param("category", "SPORTS");

        /// Act & Assert
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldShowAddCarForm() throws Exception {
        mockMvc.perform(get("/addCar"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("addCar"))
                .andExpect(model().attributeExists("carViewModel"));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldShowAdminCarDetails() throws Exception {
        /// Arrange
        Car car = testHelper.createCar();
        List<Race> races = Arrays.asList(testHelper.createRace());
        List<Sponsor> sponsors = Arrays.asList(testHelper.createSponsor());

        when(carService.getCarById(anyInt())).thenReturn(car);
        when(carService.getRacesByCarId(anyInt())).thenReturn(races);
        when(carService.getSponsorsByCarId(anyInt())).thenReturn(sponsors);
        when(authorizationService.canEditOrDeleteCar(any(), any())).thenReturn(true);

        /// Act & Assert
        mockMvc.perform(get("/car/{id}", car.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("carDetails"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("canModify"))
                .andExpect(model().attributeExists("races"))
                .andExpect(model().attributeExists("sponsors"))
                .andExpect(model().attributeExists("availableRaces"));
    }

    @Test
    @WithUserDetails(value = NORMAL_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAllowUserToViewCarDetails() throws Exception {
        /// Arrange
        Car car = testHelper.createCar();
        List<Race> races = Arrays.asList(testHelper.createRace());
        List<Sponsor> sponsors = Arrays.asList(testHelper.createSponsor());

        when(carService.getCarById(anyInt())).thenReturn(car);
        when(carService.getRacesByCarId(anyInt())).thenReturn(races);
        when(carService.getSponsorsByCarId(anyInt())).thenReturn(sponsors);
        when(authorizationService.canEditOrDeleteCar(any(), any())).thenReturn(true);

        /// Act & Assert
        mockMvc.perform(get("/car/{id}", car.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("carDetails"))
                .andExpect(model().attribute("car", car))
                .andExpect(model().attribute("canModify", true))
                .andExpect(model().attribute("races", races))
                .andExpect(model().attribute("sponsors", sponsors))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("availableRaces"));
    }

    @Test
    void shouldNotAllowAnonymousUserToViewCarDetails() throws Exception {
        /// Arrange
        Car car = testHelper.createCar();

        /// Act & Assert
        mockMvc.perform(get("/car/{id}", car.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
