package org.example.repository;

import org.example.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<ApplicationUser, Integer> {

    @Query("SELECT u FROM ApplicationUser u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<ApplicationUser> findByEmail(String email);

}
