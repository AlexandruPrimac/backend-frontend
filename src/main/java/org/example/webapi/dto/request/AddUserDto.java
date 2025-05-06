package org.example.webapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddUserDto( @NotBlank
                          String firstName,
                          @NotNull
                          String lastName,
                          @NotNull String email,
                          @NotNull String password) {

}
