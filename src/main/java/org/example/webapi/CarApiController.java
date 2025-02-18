package org.example.webapi;

import org.example.exception.CustomApplicationException;
import org.example.service.CarServiceClass;
import org.example.service.Interfaces.CarService;
import org.example.webapi.dto.CarDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/cars")
public class CarApiController {
    private final CarService carService;

    public CarApiController(CarServiceClass carService) {
        this.carService = carService;
    }

    //Filter cars by brand
    @GetMapping
    public ResponseEntity<List<CarDto>> filter(@RequestParam("brand") final String brand) {
        final List<CarDto> car = carService.filterCarsDynamically(brand).stream().map(CarDto::fromCar).toList();
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
}
