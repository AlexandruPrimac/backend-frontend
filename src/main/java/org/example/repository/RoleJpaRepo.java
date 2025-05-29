package org.example.repository;

import org.example.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepo extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);
}
