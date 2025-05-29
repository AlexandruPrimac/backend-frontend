package org.example.webapi;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.domain.Race;
import org.example.exception.CustomApplicationException;
import org.example.service.Interfaces.RaceService;
import org.example.webapi.dto.request.AddRaceDto;
import org.example.webapi.dto.request.PatchRaceDto;
import org.example.webapi.dto.response.RaceDto;
import org.example.webapi.dto.response.RaceMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/races")
public class RaceApiController {
    /// Services
    private final RaceService raceService;

    /// Mappers
    private final RaceMapper raceMapper;

    public RaceApiController(RaceService raceService, RaceMapper raceMapper) {
        this.raceService = raceService;
        this.raceMapper = raceMapper;
    }

    @GetMapping
    public ResponseEntity<List<RaceDto>> filter(@RequestParam("location") final String location) {
        final List<RaceDto> race = raceService.filterRacesDinamically(location).stream().map(raceMapper::toRaceDto).toList();

        return ResponseEntity.ok(race);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable int id) {
        try {
            raceService.deleteRace(id);
            return ResponseEntity.noContent().build();

        } catch (CustomApplicationException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<RaceDto> addRace(@Valid @RequestBody final AddRaceDto raceDto) {
        final Race race = raceService.add(raceDto.name(), raceDto.date(), raceDto.track(), raceDto.location(), raceDto.distance());

        return ResponseEntity.status(HttpStatus.CREATED).body(raceMapper.toRaceDto(race));  /// 201 response with created resource
    }

    @PatchMapping("{id}")
    public ResponseEntity<RaceDto> updateRace(@PathVariable int id, @RequestBody @Valid final PatchRaceDto patchRace) {
        try {
            Race updateRace = raceService.patch(id, patchRace.name(), patchRace.date(), patchRace.track(), patchRace.location(), patchRace.distance());
            return ResponseEntity.ok(raceMapper.toRaceDto(updateRace));

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
