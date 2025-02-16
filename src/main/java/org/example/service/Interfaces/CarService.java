package org.example.service.Interfaces;

import org.example.domain.*;
import org.example.presentation.CarViewModel;

import java.util.List;

public interface CarService {

    List<Car> getAllCars();

    Car addCar(CarViewModel carViewModel);

    List<Car> filterCars(String brand);

    List<Car> filterCarsDynamically(String brand);

    Car getCarById(int id);

    void deleteCar(int id);

    List<Race> getRacesByCarId(int carId);

    List<Sponsor> getSponsorsByCarId(int carId);

}
