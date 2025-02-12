package org.example.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class RaceViewModel {

    @NotEmpty(message = "Race name is required")
    private String name;

    @NotNull(message = "Race date is required")
    private LocalDate date;

    @NotEmpty(message = "Track is required")
    private String track;

    @NotEmpty(message = "Location is required")
    private String location;

    @NotNull(message = "Distance is required")
    @Min(value = 0, message = "Distance should be a positive number.")
    private double distance;

    private String image;

    // Getters and setters

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

    public void setTrack(String track) {this.track = track;}

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}

    public double getDistance() {return distance;}

    public void setDistance(double distance) {this.distance = distance;}

    public String getImage() { return image; }

    public void setImage(String image) {this.image = image;}
}
