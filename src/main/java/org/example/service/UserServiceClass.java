package org.example.service;

import org.example.domain.ApplicationUser;
import org.example.domain.Role;
import org.example.repository.RoleJpaRepo;
import org.example.repository.UserJpaRepo;
import org.example.service.Interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Set;

@Service
public class UserServiceClass implements UserService {
    /// Logger
    private final static Logger logger = LoggerFactory.getLogger(UserServiceClass.class);

    /// Password encoder
    private final PasswordEncoder passwordEncoder;

    /// Repositories
    private final UserJpaRepo userRepository;
    private final RoleJpaRepo roleRepository;

    @Autowired
    public UserServiceClass(UserJpaRepo userRepository, PasswordEncoder passwordEncoder, RoleJpaRepo roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public ApplicationUser add(String firstName, String lastName, String email, String password) {
        ApplicationUser user = new ApplicationUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        /// Assign default role when creating a new account
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Set.of(userRole));

        ApplicationUser savedUser = userRepository.save(user);
        logger.info("Added user: {}", savedUser);
        return savedUser;
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
