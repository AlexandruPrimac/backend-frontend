package org.example.service.Interfaces;

import org.example.domain.ApplicationUser;
import org.example.domain.Car;

public interface AuthorizationService {
    boolean canEditOrDeleteCar(ApplicationUser user, Car car);

    boolean isAdmin(ApplicationUser user);

    boolean isOwner(ApplicationUser user, Car car);
}
