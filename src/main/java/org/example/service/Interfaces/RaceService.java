package org.example.service.Interfaces;

import org.example.domain.Car;
import org.example.domain.Race;
import org.example.presentation.RaceViewModel;

import java.util.List;


public interface RaceService {

    List<Race> getAllRaces();

    Race addRace(RaceViewModel raceViewModel);

    List<Race> filterRacesDinamically(String location);

    void deleteRace(int id);

    Race getRaceById(int id);

    List<Car> getCarsByRaceId(int raceId);

}
