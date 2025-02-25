package org.example.service.Interfaces;

import org.example.domain.Car;
import org.example.domain.CarCategory;
import org.example.domain.Race;
import org.example.domain.Sponsor;
import org.example.presentation.CarViewModel;

import java.util.List;

public interface CarService {

    List<Car> getAllCars();

    List<Car> filterCarsDynamically(String brand);

    Car getCarById(int id);

    void deleteCar(int id);

    List<Race> getRacesByCarId(int carId);

    List<Sponsor> getSponsorsByCarId(int carId);

    Car add(String brand, String model, Double engineCapacity, int horsepower, int year, CarCategory category);

}
