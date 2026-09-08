package com.example.demo;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan("com.spms.model")
@EnableJpaRepositories("com.spms.repository")
@ComponentScan(basePackages = {"com.example.demo", "com.spms"})
public class StudentPerformanceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentPerformanceSystemApplication.class, args);
    }
}
