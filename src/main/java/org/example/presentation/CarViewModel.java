package org.example.presentation;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.Car;
import org.example.domain.CarCategory;

@Setter
@Getter
public class CarViewModel {

    private String brand;

    private String model;

    private double engine;

    private int horsepower;

    private int year;

    private CarCategory category;

    private String image;

    public CarViewModel(Car car) {
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.engine = car.getEngine();
        this.horsepower = car.getHorsepower();
        this.year = car.getYear();
        this.category = car.getCategory();
        this.image = car.getImage();
    }

    public CarViewModel() {
    }
}
