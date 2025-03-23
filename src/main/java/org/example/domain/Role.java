package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;  // Example: "ROLE_ADMIN", "ROLE_USER"

    @ManyToMany(mappedBy = "roles")
    private Set<ApplicationUser> users;


    @Override
    public String toString() {
        if (name.startsWith("ROLE_")) {
            return name.substring(5);  // Removes "ROLE_" prefix
        }
        return name;
    }
}
