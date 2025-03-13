package org.example.service;

import org.example.domain.ApplicationUser;
import org.example.repository.UserJpaRepo;
import org.example.service.Interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class UserServiceClass implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceClass.class);

    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepo userRepository;

    @Autowired
    public UserServiceClass(UserJpaRepo userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public ApplicationUser getCurrentUser(int id) {
        return userRepository.getById(id);
    }

    @Override
    public ApplicationUser add(String firstName, String lastName, String email, String password) {
        ApplicationUser user = new ApplicationUser();
        logger.info("Adding user: {}", user);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public ApplicationUser findUserById(int id) {
        ApplicationUser user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", id);
                    return new NotFoundException("User not found with id: " + id);
                });

        logger.info("user: {}", user);
        return user;
    }
}
