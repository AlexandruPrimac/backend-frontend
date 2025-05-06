package org.example.service.Interfaces;

import org.example.domain.ApplicationUser;
import org.example.domain.Car;

public interface AuthorizationService {
    boolean canEditOrDeleteCar(ApplicationUser user, Car car);
}
