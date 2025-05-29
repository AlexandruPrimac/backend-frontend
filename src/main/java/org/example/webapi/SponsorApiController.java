package org.example.webapi;

import org.example.exception.CustomApplicationException;
import org.example.service.Interfaces.SponsorService;
import org.example.webapi.dto.response.SponsorDto;
import org.example.webapi.dto.response.SponsorMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/sponsors")
public class SponsorApiController {
    /// Services
    private final SponsorService sponsorService;

    /// Mappers
    private final SponsorMapper sponsorMapper;

    public SponsorApiController(SponsorService sponsorService, SponsorMapper sponsorMapper) {
        this.sponsorService = sponsorService;
        this.sponsorMapper = sponsorMapper;
    }

    @GetMapping
    public ResponseEntity<List<SponsorDto>> filter(@RequestParam("name") final String name) {
        final List<SponsorDto> sponsor = sponsorService.filterSponsorsDynamically(name).stream().map(sponsorMapper::toSponsorDto).toList();

        return ResponseEntity.ok(sponsor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable int id) {
        try {
            sponsorService.deleteSponsor(id);
            return ResponseEntity.noContent().build();

        } catch (CustomApplicationException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
