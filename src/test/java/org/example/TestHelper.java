package org.example;

import org.example.domain.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class TestHelper {

    public static final String ADMIN_EMAIL = "sponge@gmail.com";

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CarJpaRepo carRepository;

    @Autowired
    private RaceJpaRepo raceRepository;

    @Autowired
    private CarRacesJpaRepo carRacesRepository;

    @Autowired
    private UserJpaRepo userRepository;

    @Autowired
    private SponsorJpaRepo sponsorRepository;

    @Autowired
    private CarOwnerShipJpaRepo carOwnerRepository;

    @Autowired
    private RoleJpaRepo roleRepository;

    public void cleanUp() {
        carRepository.deleteAll();
        raceRepository.deleteAll();
        carRacesRepository.deleteAll();
        userRepository.deleteAll();
        sponsorRepository.deleteAll();
        carOwnerRepository.deleteAll();
    }

    public Race createRace() {
        final Race race = new Race();
        race.setName("F1 GP Hungaroring");
        race.setDate(LocalDate.of(2024, 10, 12));
        race.setTrack("Hungaroring Circuit");
        race.setLocation("Hungary");
        race.setDistance(4.38);
        return raceRepository.save(race);
    }

    public Car createCar() {
        final Car car = new Car();
        car.setBrand("Alpine");
        car.setModel("A525");
        car.setEngine(1.6);
        car.setHorsepower(1000);
        car.setYear(2025);
        car.setCategory(CarCategory.F1);
        return carRepository.save(car);
    }

    public ApplicationUser createNormalUser() {
        final ApplicationUser user = new ApplicationUser();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test@gmail.com");
        user.setPassword("test");
        return userRepository.save(user);
    }

    public ApplicationUser createAdmin() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_ADMIN");
                    return roleRepository.save(newRole);
                });

        ApplicationUser user = new ApplicationUser();
        user.setEmail(ADMIN_EMAIL);
        user.setPassword(passwordEncoder.encode("admin"));
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setRoles(Set.of(adminRole));
        userRepository.save(user);
        return user;
    }


}
