package com.example.bookstore;

// Importing Spring Boot's core classes
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication is a convenience annotation that includes @Configuration, @EnableAutoConfiguration, and @ComponentScan.
// It marks this class as the main entry point for the Spring Boot application.
@SpringBootApplication
public class BookstoreApplication {

    // The main method serves as the entry point of the application.
    // SpringApplication.run() starts the Spring Boot application.
    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }
}
