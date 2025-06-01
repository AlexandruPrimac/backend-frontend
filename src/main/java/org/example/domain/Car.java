package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "cars")
public class Car {

    /// Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    @Min(0)
    private double engine;

    @Column(nullable = false)
    @Min(0)
    private int horsepower;

    @Column(name = "productionYear", nullable = false)
    @Min(1885)
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) /// Store as String in the database
    private CarCategory category;

    @Column(name = "image")
    private String image;

    /// Relationships
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private List<CarRaces> races = new ArrayList<>();

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private List<CarSponsors> sponsors = new ArrayList<>();


    /// Constructors
    public Car(int id, String brand, String model, double engine, int horsepower, int year, CarCategory category, String image) {
        this(brand, model, engine, horsepower, year, category, image);
        this.id = id;

    }

    public Car(String brand, String model, double engine, int horsepower, int year, CarCategory category, String image) {
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.horsepower = horsepower;
        this.year = year;
        this.category = category;
        this.races = new ArrayList<>();
        this.sponsors = new ArrayList<>();
        this.image = image;
    }

    public Car() {

    }


    @Override
    public String toString() {
        return String.format("Car(id=%d, brand='%s', model='%s', engine='%s', horsepower=%d, year=%d, category='%s')",
                this.getId(),
                this.getBrand(),
                this.getModel(),
                this.getEngine(),
                this.getHorsepower(),
                this.getYear(),
                this.getCategory()
        );
    }
}
