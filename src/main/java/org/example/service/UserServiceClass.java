package org.example.service;

import org.example.domain.ApplicationUser;
import org.example.repository.UserJpaRepo;
import org.example.service.Interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceClass implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceClass.class);

    private final UserJpaRepo userRepository;

    @Autowired
    public UserServiceClass(UserJpaRepo userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public ApplicationUser getCurrentUser(int id) {
        return userRepository.getById(id);
    }
}
