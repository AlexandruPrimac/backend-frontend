package org.example.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private double engine;

    @Column(nullable = false)
    private int horsepower;

    @Column(name = "productionYear", nullable = false)
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)// Store as String in the database
    private CarCategory category;

    @Column(name = "image")
    private String image;

    // Relationships
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private List<CarRaces> races = new ArrayList<>();;

    @OneToMany(mappedBy = "car" ,fetch = FetchType.LAZY)
    private List<CarSponsors> sponsors = new ArrayList<>();


    // Constructors
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

    //Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getEngine() {
        return engine;
    }

    public void setEngine(double engine) {
        this.engine = engine;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(int horsepower) {
        this.horsepower = horsepower;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CarRaces> getRaces() {
        return races;
    }

    public void setRaces(List<CarRaces> races) {
        this.races = races;
    }

    public List<CarSponsors> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<CarSponsors> sponsors) {
        this.sponsors = sponsors;
    }

//    @Override
//    public String toString() {
//        return "The car has the id " + id +
//                ", the brand '" + brand + '\'' +
//                ", the model is named '" + model + '\'' +
//                ", the engine of the car has a capacity of " + engine + "L" +
//                ", and it produces " + horsepower + " horsepower. " +
//                "The production year of the car is " + year +
//                ", and it belongs to the " + category + " category.";
//    }


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
