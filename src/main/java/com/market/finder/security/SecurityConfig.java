package com.market.finder.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SRP: Sole responsibility is Spring Security authorization matrix configuration.
 * Enforces the exact Role Access Matrix for ADMIN, INSTRUCTOR, and STUDENT.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http.authenticationProvider(authenticationProvider());

            http.authorizeHttpRequests(configurer ->
                    configurer
                            // --- Public resources ---
                            .requestMatchers("/login", "/css/**", "/img/**", "/js/**").permitAll()
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**").permitAll()
                            .requestMatchers("/error", "/access-denied").permitAll()

                            // --- Admin management endpoints (checked dynamically via MANAGE_USERS authority) ---
                            .requestMatchers("/admin/**", "/users/**", "/roles/**", "/permissions/**").hasAnyAuthority("MANAGE_USERS", "ROLE_ADMIN")


                            // --- All protected endpoints require authentication (interceptor enforces dynamic permissions) ---
                            .anyRequest().authenticated()
            );


            // Custom login form matching POST /login
            http.formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error=true")
                    .permitAll()
            );

            // Logout configuration
            http.logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .permitAll()
            );

            // Access denied handler
            http.exceptionHandling(exception -> exception
                    .accessDeniedPage("/access-denied")
            );

            http.csrf(AbstractHttpConfigurer::disable);

            return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}