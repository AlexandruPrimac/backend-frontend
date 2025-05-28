package org.example.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity security) throws Exception {
        return security
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                )
                .authorizeHttpRequests(auths -> auths
                        // Public Endpoints (Guest)
                        .requestMatchers(HttpMethod.GET, "/", "/register", "/cars", "/races", "/sponsors").permitAll()

                        // Add API endpoints for use in seperate client project
                        .requestMatchers("api/cars/**").permitAll()

                        // Endpoints for authenticated users (User)
                        .requestMatchers(HttpMethod.GET, "/car/**", "/race/**", "/user/details").authenticated()

                        // Allow user registration
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/cars/**/add-race").authenticated()

                        .requestMatchers(HttpMethod.GET, "/addCar").authenticated()

                        .requestMatchers(HttpMethod.GET, "/addRace").hasRole("ADMIN")

                        // Static Resources (Public)
                        .requestMatchers(
                                antMatcher("/js/**"),
                                antMatcher("/css/**"),
                                antMatcher("/webjars/**")).permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    if (request.getRequestURI().startsWith("/api")) {
                                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                        response.setContentType("application/json");
                                        response.getWriter().write("{\"message\":\"Not authenticated\"}");
                                    } else {
                                        response.sendRedirect("/login");
                                    }
                                })
                                // Handling 403 (Forbidden) error
                                .accessDeniedPage("/forbidden")
                )
                .httpBasic(httpBasic -> {
                }) // Enable Basic Authentication
                .formLogin(
                        login -> login
                                .loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/", true)
                                .successHandler((request, response, authentication) -> {
                                    // Force session creation
                                    HttpSession session = request.getSession(true);
                                    session.setAttribute("SPRING_SECURITY_CONTEXT",
                                            SecurityContextHolder.getContext());
                                    response.sendRedirect("/");
                                })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/cars/**"))
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
