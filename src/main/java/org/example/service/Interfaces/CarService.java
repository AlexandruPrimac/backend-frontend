package org.example.service.Interfaces;

import org.example.domain.Car;
import org.example.domain.CarCategory;
import org.example.domain.Race;
import org.example.domain.Sponsor;

import java.util.List;

public interface CarService {

    List<Car> getAllCars();

    Car add(String brand, String model, Double engineCapacity, int horsepower, int year, CarCategory category, final int userId);

    Car addCarClient(String brand, String model, Double engineCapacity, int horsepower, int year, CarCategory category);

    List<Car> filterCarsDynamically(String brand);

    Car getCarById(int id);

    void deleteCar(int id);

    List<Race> getRacesByCarId(int carId);

    List<Sponsor> getSponsorsByCarId(int carId);

    Car patch(int id, String brand, String model, double engine, int horsepower, int year, CarCategory category);

    Car addRaceToCar(int carId, int raceId);

    Car addSponsorToCar(int carId, int sponsorId);
}
