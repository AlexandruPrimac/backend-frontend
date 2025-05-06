package org.example.controller;

import org.example.domain.*;
import org.example.exception.CustomApplicationException;
import org.example.exception.DatabaseException;
import org.example.presentation.CarViewModel;
import org.example.security.CustomUserDetails;
import org.example.service.Interfaces.AuthorizationService;
import org.example.service.Interfaces.CarService;
import org.example.service.Interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Controller
public class CarController {

    private final static Logger logger = LoggerFactory.getLogger(CarController.class);
    private final CarService carService;
    private final AuthorizationService authorizationService;
    private final UserService userService;

    @Autowired
    public CarController(CarService carService, AuthorizationService authorizationService, UserService userService) {
        this.carService = carService;
        this.authorizationService = authorizationService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/cars")
    public String getAllCars(Model model) {
        try {
            List<Car> cars = carService.getAllCars();
            logger.info("Found {} cars", cars.size());
            model.addAttribute("cars", cars);
        } catch (DatabaseException ex) {
            logger.error("Database error while fetching cars: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching cars: {}", ex.getMessage());
            throw new CustomApplicationException("Unable to fetch cars due to an unexpected error.");
        }
        return "cars"; // This corresponds to the Thymeleaf view (cars.html)
    }

    @GetMapping("/addCar")
    public String showAddCarForm(Model model) {
        try {
            model.addAttribute("carViewModel", new CarViewModel());
        } catch (Exception ex) {
            logger.error("Unexpected error while showing add car form: {}", ex.getMessage());
            throw new CustomApplicationException("Unable to display the add car form.");
        }
        return "addCar";
    }


    @GetMapping("/car/{id}")
    public String getCarDetails(@PathVariable int id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            ApplicationUser user = userService.findUserById(userDetails.getId());
            Car car = carService.getCarById(id);
            boolean canModify = authorizationService.canEditOrDeleteCar(user, car);
            logger.info("Fetched car details: {}", car);
            List<CarCategory> categories = Arrays.asList(CarCategory.values());
            model.addAttribute("car", car);
            model.addAttribute("categories", categories);
            model.addAttribute("canModify", canModify);

            List<Race> races = carService.getRacesByCarId(id);
            logger.info("Fetched races for car ID {}: {}", id, races);
            model.addAttribute("races", races);

            // Filter races that the car is not participating in
            List<Race> availableRaces = races.stream()
                    .filter(race -> car.getRaces().stream().noneMatch(cr -> cr.getRace().getId() == race.getId()))
                    .toList();

            model.addAttribute("car", car);
            model.addAttribute("availableRaces", availableRaces);

            List<Sponsor> sponsors = carService.getSponsorsByCarId(id);
            logger.info("Fetched sponsors for car ID {}: {}", id, sponsors);
            model.addAttribute("sponsors", sponsors);

        } catch (DatabaseException ex) {
            logger.error("Database exception while fetching car details: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected exception while fetching car details: {}", ex.getMessage());
            throw new CustomApplicationException("Failed to fetch car details.");
        }
        return "carDetails";
    }


    @GetMapping("/testDatabaseException")
    public String testException() {
        throw new DatabaseException("Simulated Database Exception");
    }

    @GetMapping("/testGeneralException")
    public String testGeneralException() {
        throw new RuntimeException("Simulated General Exception");
    }


}
