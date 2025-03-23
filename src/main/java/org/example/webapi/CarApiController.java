package org.example.webapi;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.domain.Car;
import org.example.exception.CustomApplicationException;
import org.example.service.Interfaces.CarService;
import org.example.service.Interfaces.RaceService;
import org.example.webapi.dto.request.AddCarDto;
import org.example.webapi.dto.request.PatchCarDto;
import org.example.webapi.dto.response.CarDto;
import org.example.webapi.dto.response.CarMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //Filter cars by brand
    @GetMapping
    public ResponseEntity<List<CarDto>> filter(@RequestParam("brand") final String brand) {
        final List<CarDto> car = carService.filterCarsDynamically(brand).stream().map(carMapper::toCarDto).toList();
        return ResponseEntity.ok(car);
    }

    //Delete a car - DELETE endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable int id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.noContent().build();  // 204 response when deleted
        } catch (CustomApplicationException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 response if not found
        }
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@RequestBody @Valid final AddCarDto carDto) {
        final Car car = carService.add(carDto.brand(), carDto.model(), carDto.engine(), carDto.horsePower(), carDto.year(), carDto.category());
        return ResponseEntity.status(HttpStatus.CREATED).body(carMapper.toCarDto(car));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable int id, @RequestBody @Valid final PatchCarDto patchCar) {
        try{
            Car updateCar = carService.patch(id, patchCar.brand(), patchCar.model(), patchCar.engine(), patchCar.horsepower(), patchCar.year(), patchCar.category());
            return ResponseEntity.ok(carMapper.toCarDto(updateCar));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PatchMapping("{id}/add-race")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarDto> addRaceToCar(@PathVariable int id, @RequestParam int raceId) {
        try {
            // Add race to car via the service
            Car updatedCar = carService.addRaceToCar(id, raceId);

            // Convert the updated Car entity to DTO
            CarDto updatedCarDto = carMapper.toCarDto(updatedCar);

            // Return the updated car with race added
            return ResponseEntity.ok(updatedCarDto);
        } catch (CustomApplicationException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

}