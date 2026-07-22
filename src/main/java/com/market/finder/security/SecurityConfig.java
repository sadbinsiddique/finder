package com.market.finder.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SRP: Sole responsibility is Spring Security configuration.
 * DIP: Depends on UserDetailsService interface, NOT concrete CustomUserDetailsService.
 */
@Configuration
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
                            .requestMatchers("/error").permitAll()

                            // --- Admin-only endpoints ---
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/users/**").hasRole("ADMIN")
                            .requestMatchers("/roles/**").hasRole("ADMIN")

                            // --- Protected API endpoints ---
                            .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER", "INSTRUCTOR", "STUDENT")
                            .requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                            .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                            .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                            // --- Everything else requires authentication ---
                            .anyRequest().authenticated()
            );

            // Custom login form
            http.formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/authenticateTheUser")
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