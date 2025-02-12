package org.example.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "races")
public class Race {

    /// Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String track;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double distance;

    @Column(name = "image")
    private String image;

    //Relationship
    @OneToMany(mappedBy = "race", fetch = FetchType.LAZY)
    private List<CarRaces> cars = new ArrayList<>();;


    // Constructors
    public Race(int id, String name, LocalDate date, String track, String location, double distance, String image) {
        this(name, date, track, location, distance, image);
        this.id = id;

    }

    public Race(String name, LocalDate date,String track, String location, double distance, String image) {
        this.name = name;
        this.date = date;
        this.track = track;
        this.location = location;
        this.distance = distance;
        this.cars = new ArrayList<>();
        this.image = image;

    }

    public Race() {
    }

    //Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CarRaces> getCars() {
        return cars;
    }

    public void setCars(List<CarRaces> cars) {
        this.cars = cars;
    }


    @Override
    public String toString() {
        return "The race has the id " + id +
                ", it is named '" + name + '\'' +
                ", the date for the event is " + date +
                " and it is held at the track " + track + ". " +
                "The location is '" + location + '\'' + ". " +
                "The race has a distance of " + distance + "km.";
    }

}
