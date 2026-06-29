package com.market.finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.market.finder")
public class FinderApplication {

    static void main(String[] args) {
        SpringApplication.run(FinderApplication.class, args);
    }

}
