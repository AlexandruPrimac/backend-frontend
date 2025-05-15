package org.example.service;

import org.example.domain.ApplicationUser;
import org.example.domain.Car;
import org.example.repository.CarOwnerShipJpaRepo;
import org.example.service.Interfaces.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceClass implements AuthorizationService {

    private final static Logger logger = LoggerFactory.getLogger(AuthorizationServiceClass.class);

    private final CarOwnerShipJpaRepo carOwnershipRepository;


    public AuthorizationServiceClass(CarOwnerShipJpaRepo carOwnershipRepository) {
        this.carOwnershipRepository = carOwnershipRepository;
    }

    @Override
    public boolean canEditOrDeleteCar(ApplicationUser user, Car car) {
        if (user == null || car == null) {
            logger.warn("User is: " + user + ", Car is: " + car + ". Cannot proceed with authorization.");
            return false;
        }

        // Check if the user has the admin role
        if (isAdmin(user)) {
            logger.info("User is admin, can edit/delete car");
            return true;
        }

        // Check if the user is the owner of the car
        return isOwner(user, car);
    }

    @Override
    public boolean isAdmin(ApplicationUser user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }


    @Override
    public boolean isOwner(ApplicationUser user, Car car) {
        return carOwnershipRepository.findByCarIdAndUserId(car.getId(), user.getId())
                .map(ownership -> {
                    logger.info("User is the owner of the car, can edit/delete car");
                    return true;
                })
                .orElseGet(() -> {
                    logger.info("User is not the owner of the car, cannot edit/delete car");
                    return false;
                });
    }
}
