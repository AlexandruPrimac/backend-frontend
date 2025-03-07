package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(auths -> auths
                        // Public Endpoints
                        .requestMatchers(HttpMethod.GET, "/", "/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cars", "/races", "/sponsors").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cars/{id}").permitAll()

                        // User-Specific API
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated() // Only logged-in users
                        .requestMatchers(HttpMethod.GET, "/api/users/test").authenticated() //

                        // Static Resources
                        .requestMatchers("/js/**", "/webjars/**", "/images/**", "/video/**", "/favicon/**", "/css/**").permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api")) {
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF (enable later if needed)
                .formLogin(
                        login -> login.loginPage("/login").permitAll()
                                .defaultSuccessUrl("/", true)
                )
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
