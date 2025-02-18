package org.example.controller;

import jakarta.validation.Valid;
import org.example.domain.Car;
import org.example.domain.Race;
import org.example.exception.CustomApplicationException;
import org.example.exception.DatabaseException;
import org.example.presentation.RaceViewModel;
import org.example.service.Interfaces.RaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RaceController {

    private final static Logger logger = LoggerFactory.getLogger(RaceController.class);
    private final RaceService raceService;

    @Autowired
    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @GetMapping("/races")
    public String getRaces(Model model) {
        try {
            logger.info("Getting all races");
            model.addAttribute("races", raceService.getAllRaces());
        } catch (DatabaseException ex) {
            logger.error("Database exception while fetching races: {}", ex.getMessage());
            throw ex; // Handled by the global exception handler
        } catch (Exception ex) {
            logger.error("Unexpected exception: {}", ex.getMessage());
            throw new CustomApplicationException("Failed to fetch races");
        }
        return "races";
    }

    @GetMapping("/addRace")
    public String addRace(Model model) {
        model.addAttribute("raceViewModel", new RaceViewModel());
        return "addRace";
    }

    @PostMapping("/addRace")
    public String addRace(@Valid @ModelAttribute("raceViewModel") RaceViewModel raceViewModel,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors while adding race: {}", bindingResult.getAllErrors());
            return "addRace";
        }

        try {
            logger.info("Adding race: {}", raceViewModel.getName());
            raceService.addRace(raceViewModel);
        } catch (DatabaseException ex) {
            logger.error("Database exception while adding race: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected exception: {}", ex.getMessage());
            throw new CustomApplicationException("Failed to add the race");
        }

        return "redirect:/races";
    }

    @GetMapping("/race/{id}")
    public String getRaceDetails(@PathVariable int id, Model model) {

        try {
            Race race = raceService.getRaceById(id);
            logger.info("Fetched race details: {}", race);
            model.addAttribute("race", race);

            List<Car> cars = raceService.getCarsByRaceId(id);
            logger.info("Cars for race: {}", cars);
            model.addAttribute("car", cars);
        } catch (DatabaseException ex) {
            logger.error("Database exception while fetching race details: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected exception: {}", ex.getMessage());
            throw new CustomApplicationException("Failed to fetch race details");
        }

        return "raceDetails";
    }


}
