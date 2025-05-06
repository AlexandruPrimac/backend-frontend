package org.example.presentation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.Car;
import org.example.domain.CarCategory;

@Setter
@Getter
public class CarViewModel {

    @NotNull(message = "Brand in required")
    @Size(max = 30, message = "{brand.size}")
    private String brand;

    @NotNull(message = "{model.required}")
    @Size(max = 30, message = "{model.size}")
    private String model;

    @NotNull(message = "{engine.required}")
    @Min(value = 0, message = "{engine.range}")
    private double engine;

    @NotNull(message = "{horsepower.required}")
    @Min(value = 10, message = "Please insert a value higher than 10")
    @Max(value = 5000, message = "{horsepower.range}")
    private int horsepower;

    @NotNull(message = "{year.required}")
    @Min(value = 1900, message = "{year.range}")
    @Max(value = 2100, message = "{year.range}")
    private int year;

    @NotNull(message = "{category.required}")
    private CarCategory category;

    private String image;


    // Constructor to map from Car domain object to ViewModel
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
