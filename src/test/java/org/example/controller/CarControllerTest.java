package org.example.controller;

import org.example.TestHelper;
import org.example.domain.Car;
import org.example.domain.Race;
import org.example.domain.Sponsor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private Car car;
    private Race race;
    private Sponsor sponsor;

    @BeforeEach
    void setUp() {
        testHelper.cleanUp();

        // Prepare users for security context
        testHelper.createNormalUser();
        testHelper.createAdmin();

        // Prepare test data in DB
        car = testHelper.createCar();
        race = testHelper.createRace();
        sponsor = testHelper.createSponsor();
    }

    @Test
    void shouldListAllCars() throws Exception {
        /// Act & Assert
        mockMvc.perform(get("/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("cars"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", hasSize(1)));
    }

    @Test
    void shouldHandleEmptyCarList() throws Exception {
        testHelper.cleanUp();

        mockMvc.perform(get("/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("cars"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", hasSize(0)));
    }

    @Test
    @WithUserDetails(value = TestHelper.ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldShowAdminCarDetails() throws Exception {
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
    @WithUserDetails(value = TestHelper.NORMAL_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAllowUserToViewCarDetails() throws Exception {
        mockMvc.perform(get("/car/{id}", car.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("carDetails"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attributeExists("canModify"))
                .andExpect(model().attributeExists("races"))
                .andExpect(model().attributeExists("sponsors"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("availableRaces"));
    }

    @Test
    void shouldNotAllowAnonymousUserToViewCarDetails() throws Exception {
        mockMvc.perform(get("/car/{id}", car.getId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
