package org.example.service.Interfaces;

import org.example.domain.ApplicationUser;

public interface UserService {

    ApplicationUser add(String firstName, String lastName, String email, String password);

    ApplicationUser findUserById(int id);
}
