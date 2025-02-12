package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "org.example.domain")
public class Runner {

    public static void main(String[] args) {
        SpringApplication.run(Runner.class, args);
        System.out.println("Web interface is running at http://localhost:8080");
    }

}
