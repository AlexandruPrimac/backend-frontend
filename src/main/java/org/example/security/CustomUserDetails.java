package org.example.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetails extends User {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;

    public CustomUserDetails(int id, String email, String firstName, String lastName,
                             String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities); // 'email' is used as the username for authentication
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
