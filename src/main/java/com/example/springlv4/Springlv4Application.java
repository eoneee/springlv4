package com.example.springlv4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Springlv4Application {

    public static void main(String[] args) {
        SpringApplication.run(Springlv4Application.class, args);
    }

}
