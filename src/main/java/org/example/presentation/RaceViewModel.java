package org.example.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
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

}
