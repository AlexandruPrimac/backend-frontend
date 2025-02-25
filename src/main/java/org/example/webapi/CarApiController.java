package org.example.webapi;

import jakarta.validation.Valid;
import org.example.domain.Car;
import org.example.exception.CustomApplicationException;
import org.example.service.CarServiceClass;
import org.example.service.Interfaces.CarService;
import org.example.webapi.dto.response.CarDto;
import org.example.webapi.dto.request.AddCarDto;
import org.example.webapi.dto.response.CarMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

}
