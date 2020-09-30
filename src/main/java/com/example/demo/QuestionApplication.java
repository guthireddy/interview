package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author Narasimha Reddy Guthireddy
 *
 */
@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.example.demo.persistence"})  // scan JPA entities
public class QuestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionApplication.class, args);
    }

}
