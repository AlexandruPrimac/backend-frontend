package org.example.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("jpa")
@EnableJpaRepositories(basePackages = "org.example.repository")
@EntityScan(basePackages = "org.example.domain.jpa")
@EnableTransactionManagement
public class JpaConfig {
    // Configuration for JPA
}
