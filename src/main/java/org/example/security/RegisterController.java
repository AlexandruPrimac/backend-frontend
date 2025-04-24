package org.example.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

//    private final UserJpaRepo userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final static Logger log = LoggerFactory.getLogger(RegisterController.class);
//
//    public RegisterController(UserJpaRepo userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

//    @PostMapping("/register")
//    @Transactional
//    public String processRegistration(
//            @Valid @ModelAttribute("user") RegistrationDto registrationDto,
//            BindingResult bindingResult,
//            RedirectAttributes redirectAttributes) {
//
//        log.info("Registration attempt for email: {}", registrationDto.getEmail());
//
//        // Password match validation
//        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
//            log.warn("Password mismatch for email: {}", registrationDto.getEmail());
//            bindingResult.rejectValue("confirmPassword", "match", "Passwords must match");
//        }
//
//        // Email uniqueness check
//        if (userRepository.existsByEmail(registrationDto.getEmail())) {
//            log.warn("Duplicate registration attempt for email: {}", registrationDto.getEmail());
//            bindingResult.rejectValue("email", "unique", "Email already registered");
//        }
//
//        if (bindingResult.hasErrors()) {
//            log.error("Validation errors during registration:");
//            bindingResult.getAllErrors().forEach(error ->
//                    log.error("Validation error: {}", error.toString())
//            );
//            return "register";
//        }
//
//        try {
//            ApplicationUser user = new ApplicationUser();
//            user.setFirstName(registrationDto.getFirstName());
//            user.setLastName(registrationDto.getLastName());
//            user.setEmail(registrationDto.getEmail());
//            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
//
//            log.debug("Attempting to save user: {}", user.toString());
//            ApplicationUser savedUser = userRepository.save(user);
//            log.info("User registered successfully with ID: {}", savedUser.getId());
//
//            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
//            return "redirect:/login";
//
//        } catch (DataAccessException e) {
//            log.error("Database error during registration: {}", e.getMessage());
//            log.debug("Stack trace:", e);
//            redirectAttributes.addFlashAttribute("error", "Registration failed. Please try again.");
//            return "redirect:/register";
//        } catch (Exception e) {
//            log.error("Unexpected error during registration: {}", e.getMessage());
//            log.debug("Stack trace:", e);
//            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
//            return "redirect:/register";
//        }
//    }
//
//    // Registration DTO
//    @Getter
//    @Setter
//    public static class RegistrationDto {
//        @NotBlank
//        private String firstName;
//        @NotBlank
//        private String lastName;
//        @NotBlank
//        @Email
//        private String email;
//        @NotBlank
//        @Size(min = 8)
//        private String password;
//        @NotBlank
//        private String confirmPassword;
//    }
}

