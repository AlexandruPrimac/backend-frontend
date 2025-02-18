package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.domain.Car;
import org.example.domain.CarRaces;
import org.example.domain.Race;
import org.example.exception.CustomApplicationException;
import org.example.presentation.RaceViewModel;
import org.example.repository.RaceJpaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RaceServiceClass implements org.example.service.Interfaces.RaceService {
    private final static Logger logger = LoggerFactory.getLogger(RaceServiceClass.class);

    private final RaceJpaRepo raceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public RaceServiceClass(RaceJpaRepo raceRepository) {
        this.raceRepository = raceRepository;
    }

    @Override
    public List<Race> getAllRaces() {
        List<Race> races = raceRepository.findAll();
        logger.info("Found {} races", races.size());

        logger.info("Races: {}", races);

        return races;
    }

    @Override
    public Race addRace(RaceViewModel raceViewModel) {
        Race race = new Race(
                raceViewModel.getName(),
                raceViewModel.getDate(),
                raceViewModel.getTrack(),
                raceViewModel.getLocation(),
                raceViewModel.getDistance(),
                raceViewModel.getImage()
        );
        logger.info("Adding race: {}", race);
        return raceRepository.save(race);
    }

    @Override
    public List<Race> filterRacesDinamically(String location) {
        return raceRepository.filterRacesByLocation(location);
    }

    @Override
    public void deleteRace(int id) {

        if (!raceRepository.existsById(id)) {
            throw new CustomApplicationException("Race not found with ID: " + id);
        }

        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new CustomApplicationException("Race not found with ID: " + id));

        entityManager.createQuery("DELETE FROM CarRaces cr WHERE cr.race = :race")
                .setParameter("race", race)
                .executeUpdate();

        // Now delete the race
        raceRepository.deleteById(id);
    }


    @Override
    public Race getRaceById(int id) {
        Optional<Race> race = raceRepository.findByIdWithDetails(id);
        if (race.isPresent()) {
            logger.info("Found race with ID: {}", id);

            logger.info("Race: {}", race.get());
            return race.get();
        } else {
            logger.warn("Race with ID: {} not found", id);
            return null;
        }
    }

    @Override
    public List<Car> getCarsByRaceId(int raceId) {
        Optional<Race> race = raceRepository.findByIdWithDetails(raceId);
        if (race.isPresent()) {
            List<Car> cars = race.get().getCars().stream().map(CarRaces::getCar).toList();
            ;
            logger.info("Found {} cars for race ID: {}", cars.size(), raceId);
            return cars;
        } else {
            logger.warn("Race with ID: {} not found", raceId);
            return List.of();
        }
    }

}
