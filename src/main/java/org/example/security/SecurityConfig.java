package org.example.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                        // Public Endpoints
                        .requestMatchers(HttpMethod.GET, "/", "/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cars", "/races", "/sponsors").permitAll()

                        // User-Specific API
                        .requestMatchers(HttpMethod.GET, "/api/cars/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated() // Only logged-in users
                        .requestMatchers(HttpMethod.GET, "/api/users/test").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        //

                        // Static Resources
                        .requestMatchers("/js/**", "/webjars/**", "/images/**", "/video/**", "/favicon/**", "/css/**").permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api")) {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                response.setContentType("application/json");
                                response.getWriter().write("{\"message\":\"Not authenticated\"}");
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF (enable later if needed)
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
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
