package com.market.finder.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Define in-memory users with roles
    @Bean
    public InMemoryUserDetailsManager userDetailManager() {
        UserDetails john = User.builder()
                .username("john")
                .password("{noop}1234") // {noop} means plain text password (fine for testing)
                .roles("EMPLOYEE")
                .build();
        UserDetails mary = User.builder()
                .username("mary")
                .password("{noop}1234")
                .roles("EMPLOYEE", "MANAGER")
                .build();
        UserDetails susan = User.builder()
                .username("susan")
                .password("{noop}1234")
                .roles("EMPLOYEE", "MANAGER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(john, mary, susan);
    }

    // Restrict URL access based on Roles
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/magic-api/employees").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/magic-api/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/magic-api/employees").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/magic-api/employees").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/magic-api/employees/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/magic-api/employees/**").hasRole("MANAGER")
        );

        // Use HTTP Basic authentication (or formLogin() depending on your needs)
        http.httpBasic(Customizer.withDefaults());

        // Disable CSRF if you are building a stateless REST API (optional, but common for testing APIs)
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}