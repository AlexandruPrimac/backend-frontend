package org.example.webapi.dto.response;

public record UserDto(int id, String firstName, String lastName, String email, String password) {
}
