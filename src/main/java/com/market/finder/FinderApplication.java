package com.market.finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        org.springdoc.core.configuration.SpringDocDataRestConfiguration.class,
        org.springdoc.core.configuration.SpringDocHateoasConfiguration.class
})
@EnableCaching
@EnableScheduling
public class FinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinderApplication.class, args);
    }
}