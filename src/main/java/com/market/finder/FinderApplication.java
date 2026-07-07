package com.market.finder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FinderApplication {
    static void main(String[] args) {
        SpringApplication.run(FinderApplication.class, args);
    }

    @Bean
    public CommandLineRunner cmdRunner() {
        return args -> {
            System.out.println("Custom Code Is Running.");
        };
    }

}



