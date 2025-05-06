package org.example.controller;

import org.example.TestHelper;
import org.example.domain.Car;
import org.example.domain.CarCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.example.TestHelper.ADMIN_EMAIL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @BeforeEach
    void setUp() {
        testHelper.cleanUp();
        testHelper.createAdmin();
    }

    @Test
    void shouldListAllCars() throws Exception {
        mockMvc.perform(get("/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("cars"))
                .andExpect(model().attributeExists("cars"));
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


}
