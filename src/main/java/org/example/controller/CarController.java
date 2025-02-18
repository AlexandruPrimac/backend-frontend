package org.example.controller;

import jakarta.validation.Valid;
import org.example.domain.*;
import org.example.exception.CustomApplicationException;
import org.example.exception.DatabaseException;
import org.example.presentation.CarViewModel;
import org.example.service.Interfaces.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CarController {

    private final static Logger logger = LoggerFactory.getLogger(CarController.class);
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String home() {
        return "index";  // Should point to the main page
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
        return "addCar"; // This corresponds to the "addCar"
    }

    @PostMapping("/addCar")
    public String addCar(@Valid @ModelAttribute("car") CarViewModel carViewModel, BindingResult errors, Model model) {
        if (carViewModel.getBrand().equalsIgnoreCase("error")) {
            throw new DatabaseException("Simulated database error: Invalid brand provided.");
        }

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> logger.error(error.toString()));
            return "addCar"; // Show the form again
        }

        try {
            logger.info("Adding car: {} {}", carViewModel.getBrand(), carViewModel.getModel());
            carService.addCar(carViewModel);
        } catch (DatabaseException ex) {
            logger.error("Database error while adding car: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while adding car: {}", ex.getMessage());
            throw new CustomApplicationException("Unable to add the car due to an unexpected error.");
        }

        return "redirect:/cars"; // Redirect to all cars after adding
    }

    @GetMapping("/car/{id}")
    public String getCarDetails(@PathVariable int id, Model model) {
        try {

            Car car = carService.getCarById(id);
            logger.info("Fetched car details: {}", car);
            model.addAttribute("car", car);

            List<Race> races = carService.getRacesByCarId(id);
            logger.info("Fetched races for car ID {}: {}", id, races);
            model.addAttribute("races", races);

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
