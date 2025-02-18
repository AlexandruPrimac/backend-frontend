package org.example.controller;

import org.example.domain.Sponsor;
import org.example.exception.CustomApplicationException;
import org.example.exception.DatabaseException;
import org.example.service.Interfaces.SponsorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SponsorController {

    private final static Logger logger = LoggerFactory.getLogger(SponsorController.class);
    private final SponsorService sponsorService;

    @Autowired
    public SponsorController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @GetMapping("/sponsors")
    public String getRaces(Model model) {

        try {
            List<Sponsor> sponsors = sponsorService.getAllSponsors();
            logger.info("Found {} sponsors", sponsors.size());
            model.addAttribute("sponsors", sponsors);
        } catch (DatabaseException ex) {
            logger.error("Database exception while fetching all sponsors: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected exception while fetching sponsors: {}", ex.getMessage());
            throw new CustomApplicationException("Failed to fetch sponsors.");
        }
        return "sponsors";
    }


}
