package com.market.finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FinderApplication {

    static void main(String[] args) {
        SpringApplication.run(FinderApplication.class, args);
    }
}