package org.example.webapi;

import org.example.repository.RaceJpaRepo;
import org.example.service.Interfaces.RaceService;
import org.example.webapi.dto.CarDto;
import org.example.webapi.dto.RaceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value ="/api/races")
public class RaceApiController {
    private final RaceService raceService;

    public RaceApiController(RaceService raceService) {
        this.raceService = raceService;
    }

    //Filter races by location
    @GetMapping
    public ResponseEntity<List<RaceDto>> filter (@RequestParam("location") final String location) {
        final List<RaceDto> race = raceService.filterRacesDinamically(location).stream().map(RaceDto::fromRace).toList();
        return ResponseEntity.ok(race);
    }
}
