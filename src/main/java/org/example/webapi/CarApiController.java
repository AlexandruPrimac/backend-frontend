package org.example.webapi;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.domain.Car;
import org.example.exception.CustomApplicationException;
import org.example.security.CustomUserDetails;
import org.example.service.Interfaces.CarService;
import org.example.webapi.dto.request.AddCarDto;
import org.example.webapi.dto.request.PatchCarDto;
import org.example.webapi.dto.response.CarDto;
import org.example.webapi.dto.response.CarMapper;
import org.example.webapi.dto.response.CarWithRacesDto;
import org.example.webapi.dto.response.CarWithSponsorsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/cars")
public class CarApiController {

    private final CarService carService;
    private final CarMapper carMapper;

    public CarApiController(CarService carService, CarMapper carMapper) {
        this.carService = carService;
        this.carMapper = carMapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<CarDto> getById(@PathVariable("id") final int id) {
        return ResponseEntity.ok(carMapper.toCarDto(carService.getCarById(id)));
    }

    //Filter cars by brand
    @GetMapping
    public ResponseEntity<List<CarDto>> filter(@RequestParam("brand") final String brand) {
        final List<CarDto> car = carService.filterCarsDynamically(brand).stream().map(carMapper::toCarDto).toList();
        return ResponseEntity.ok(car);
    }

    //Delete a car - DELETE endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable int id, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.noContent().build();  // 204 response when deleted
        } catch (CustomApplicationException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 response if not found
        }
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@RequestBody @Valid final AddCarDto carDto, @AuthenticationPrincipal CustomUserDetails user) {
        System.out.println("User who is adding car: " + user.getEmail());
        final Car car = carService.add(carDto.brand(), carDto.model(), carDto.engine(), carDto.horsePower(), carDto.year(), carDto.category(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(carMapper.toCarDto(car));
    }

    @PostMapping("/client")
    public ResponseEntity<CarDto> addCarClient(@RequestBody @Valid final AddCarDto carDto) {
        final Car car = carService.addCarClient(carDto.brand(), carDto.model(), carDto.engine(), carDto.horsePower(), carDto.year(), carDto.category());
        return ResponseEntity.status(HttpStatus.CREATED).body(carMapper.toCarDto(car));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable int id, @RequestBody @Valid final PatchCarDto patchCar) {
        try {
            Car updateCar = carService.patch(id, patchCar.brand(), patchCar.model(), patchCar.engine(), patchCar.horsepower(), patchCar.year(), patchCar.category());
            return ResponseEntity.ok(carMapper.toCarDto(updateCar));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PatchMapping("{id}/add-race")
    public ResponseEntity<CarWithRacesDto> addRaceToCar(@PathVariable int id, @RequestParam int raceId) {
        try {
            Car updatedCar = carService.addRaceToCar(id, raceId);
            CarWithRacesDto updatedCarDto = carMapper.toCarWithRacesDto(updatedCar);
            return ResponseEntity.ok(updatedCarDto);
        } catch (CustomApplicationException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("{id}/add-sponsor")
    public ResponseEntity<CarWithSponsorsDto> addSponsorToCar(@PathVariable int id, @RequestParam int sponsorId) {
        try {
            Car updatedCar = carService.addSponsorToCar(id, sponsorId);
            CarWithSponsorsDto updatedCarDto = carMapper.toCarWithSponsorsDto(updatedCar);
            return ResponseEntity.ok(updatedCarDto);
        } catch (CustomApplicationException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}