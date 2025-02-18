package org.example.webapi;

import org.example.exception.CustomApplicationException;
import org.example.service.Interfaces.SponsorService;
import org.example.webapi.dto.SponsorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/sponsors")
public class SponsorApiController {

    private final SponsorService sponsorService;

    public SponsorApiController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    //Filter sponsors by name
    @GetMapping
    public ResponseEntity<List<SponsorDto>> filter(@RequestParam("name") final String name) {
        final List<SponsorDto> sponsor = sponsorService.filterSponsorsDynamically(name).stream().map(SponsorDto::fromSponsor).toList();
        return ResponseEntity.ok(sponsor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable int id) {
        try {
            sponsorService.deleteSponsor(id);
            return ResponseEntity.noContent().build();  // 204 response when deleted
        } catch (CustomApplicationException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 response if not found
        }
    }
}
