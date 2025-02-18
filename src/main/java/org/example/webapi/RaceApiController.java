package org.example.webapi;

import org.example.exception.CustomApplicationException;
import org.example.service.Interfaces.RaceService;
import org.example.webapi.dto.RaceDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable int id) {
        try {
            raceService.deleteRace(id);
            return ResponseEntity.noContent().build();  // 204 response when deleted
        } catch (CustomApplicationException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 response if not found
        }
    }

}
