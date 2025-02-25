package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "races")
public class Race {

    // Attributes
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
