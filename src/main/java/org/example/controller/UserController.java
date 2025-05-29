package org.example.controller;

import org.example.domain.ApplicationUser;
import org.example.security.CustomUserDetails;
import org.example.service.Interfaces.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    /// Services
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/details")
    public String userPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        ApplicationUser user = userService.findUserById(userDetails.getId());

        model.addAttribute("role", user.getRoles());
        model.addAttribute("user", user);

        return "userDetails";
    }
}
