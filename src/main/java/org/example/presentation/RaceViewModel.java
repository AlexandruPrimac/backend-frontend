package org.example.presentation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class RaceViewModel {

    private String name;

    private LocalDate date;

    private String track;

    private String location;

    private double distance;

    private String image;

}
