package org.example.service.Interfaces;

import org.example.domain.Car;
import org.example.domain.Race;
import org.example.presentation.RaceViewModel;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public interface RaceService {

    List<Race> getAllRaces();

    List<Race> filterRacesDinamically(String location);

    void deleteRace(int id);

    Race getRaceById(int id);

    List<Car> getCarsByRaceId(int raceId);

    Race add(String name, LocalDate date, String track, String location, double distance);

}
