package org.example.webapi;

import org.example.service.Interfaces.SponsorService;
import org.example.webapi.dto.SponsorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/sponsors")
public class SponsorApiController {

    private final SponsorService sponsorService;

    public SponsorApiController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @GetMapping
    public ResponseEntity<List<SponsorDto>> filter (@RequestParam("name") final String name) {
        final List<SponsorDto> sponsor = sponsorService.filterSponsorsDynamically(name).stream().map(SponsorDto::fromSponsor).toList();
        return ResponseEntity.ok(sponsor);
    }
}
