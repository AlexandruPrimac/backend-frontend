package org.example.controller;

import org.example.domain.*;
import org.example.exception.CustomApplicationException;
import org.example.exception.DatabaseException;
import org.example.presentation.CarViewModel;
import org.example.security.CustomUserDetails;
import org.example.service.Interfaces.*;
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

    /// Logger
    private final static Logger logger = LoggerFactory.getLogger(CarController.class);

    /// Services
    private final CarService carService;
    private final RaceService raceService;
    private final SponsorService sponsorService;
    private final AuthorizationService authorizationService;
    private final UserService userService;

    @Autowired
    public CarController(CarService carService, RaceService raceService, SponsorService sponsorService, AuthorizationService authorizationService, UserService userService) {
        this.carService = carService;
        this.raceService = raceService;
        this.sponsorService = sponsorService;
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
        return "cars";
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

            /// Fetch car normally without fetching collections
            Car car = carService.getCarById(id);

            /// Initialize races separately
            List<Race> races = carService.getRacesByCarId(id);

            /// Initialize sponsors separately
            List<Sponsor> sponsors = carService.getSponsorsByCarId(id);

            /// Authorization check
            boolean canModify = authorizationService.canEditOrDeleteCar(user, car);
            logger.info("Fetched car details: {}", car);

            List<CarCategory> categories = Arrays.asList(CarCategory.values());
            model.addAttribute("car", car);
            model.addAttribute("categories", categories);
            model.addAttribute("canModify", canModify);

            model.addAttribute("races", races);

            /// Filter races not linked to car
            List<Race> allRaces = raceService.getAllRaces();
            List<Race> availableRaces = allRaces.stream()
                    .filter(race -> races.stream().noneMatch(r -> r.getId() == race.getId()))
                    .toList();

            logger.info("Available races count: {}", availableRaces.size());
            logger.info("Available races: {}", availableRaces);

            model.addAttribute("availableRaces", availableRaces);

            model.addAttribute("sponsors", sponsors);

            /// Filter sponsors not linked to car
            List<Sponsor> allSponsors = sponsorService.getAllSponsors();
            List<Sponsor> availableSponsors = allSponsors.stream()
                    .filter(sponsor -> sponsors.stream().noneMatch(s -> s.getId() == sponsor.getId()))
                    .toList();

            logger.info("Available sponsors count: {}", availableSponsors.size());
            logger.info("Available sponsors: {}", availableSponsors);

            model.addAttribute("availableSponsors", availableSponsors);

        } catch (DatabaseException ex) {
            logger.error("Database exception while fetching car details: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected exception while fetching car details: {}", ex.getMessage());
            throw new CustomApplicationException("Failed to fetch car details.");
        }
        return "carDetails";
    }


    /// Testing Exceptions
    @GetMapping("/testDatabaseException")
    public String testException() {
        throw new DatabaseException("Simulated Database Exception");
    }

    @GetMapping("/testGeneralException")
    public String testGeneralException() {
        throw new RuntimeException("Simulated General Exception");
    }


}
