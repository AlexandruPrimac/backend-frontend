package org.example.webapi;

import jakarta.validation.Valid;
import org.example.domain.ApplicationUser;
import org.example.service.Interfaces.UserService;
import org.example.webapi.dto.request.AddUserDto;
import org.example.webapi.dto.response.UserDto;
import org.example.webapi.dto.response.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserApiController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserApiController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal ApplicationUser user) {
        System.out.println("Current auth: " + (user != null ? user.getEmail() : "null"));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDto userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testAuth(@AuthenticationPrincipal ApplicationUser user) {
        return user != null ?
                ResponseEntity.ok("Authenticated as: " + user.getEmail()) :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody final AddUserDto userDto) {
        final ApplicationUser user = userService.add(userDto.firstName(), userDto.lastName(), userDto.email(), userDto.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));  // 201 response with created resource
    }
}
