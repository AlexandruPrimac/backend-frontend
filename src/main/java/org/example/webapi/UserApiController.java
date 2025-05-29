package org.example.webapi;

import jakarta.validation.Valid;
import org.example.domain.ApplicationUser;
import org.example.service.Interfaces.UserService;
import org.example.webapi.dto.request.AddUserDto;
import org.example.webapi.dto.response.UserDto;
import org.example.webapi.dto.response.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserApiController {
    /// Services
    private final UserService userService;

    /// Mappers
    private final UserMapper userMapper;

    public UserApiController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody final AddUserDto userDto) {
        final ApplicationUser user = userService.add(userDto.firstName(), userDto.lastName(), userDto.email(), userDto.password());

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }
}
