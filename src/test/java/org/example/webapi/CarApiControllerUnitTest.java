package org.example.webapi;

import org.example.service.Interfaces.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CarApiControllerUnitTest {
    @Autowired
    private CarApiController sut;

    @MockBean
    private CarService carService;


}
