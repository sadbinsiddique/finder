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
 * SRP: Sole responsibility is Spring Security authorization matrix configuration.
 * Enforces the exact Role Access Matrix for ADMIN, INSTRUCTOR, and STUDENT.
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
                            .requestMatchers("/error", "/access-denied").permitAll()

                            // --- Admin-only management endpoints ---
                            .requestMatchers("/admin/**", "/users/**", "/roles/**").hasRole("ADMIN")

                            // =====================================================
                            // 1. INSTRUCTORS (Admin: Full, Student: View Only, Instructor: NO ACCESS)
                            // =====================================================
                            .requestMatchers(HttpMethod.GET, "/instructors", "/instructors/").hasAnyRole("ADMIN", "STUDENT")
                            .requestMatchers("/instructors/**").hasRole("ADMIN")

                            // =====================================================
                            // 2. COURSES (Admin: Full, Student: View Only, Instructor: NO ACCESS)
                            // =====================================================
                            .requestMatchers(HttpMethod.GET, "/courses", "/courses/").hasAnyRole("ADMIN", "STUDENT")
                            .requestMatchers("/courses/**").hasRole("ADMIN")

                            // =====================================================
                            // 3. DEPARTMENTS (Admin: Full, Instructor: View & Update, Student: View Only)
                            // =====================================================
                            .requestMatchers("/departments/delete/**").hasRole("ADMIN")
                            .requestMatchers("/departments/new").hasRole("ADMIN")
                            .requestMatchers("/departments/edit/**", "/departments/save").hasAnyRole("ADMIN", "INSTRUCTOR")
                            .requestMatchers("/departments", "/departments/").hasAnyRole("ADMIN", "INSTRUCTOR", "STUDENT")

                            // =====================================================
                            // 4. STUDENTS (Admin: Full, Instructor & Student: View, Create, Edit - NO Delete)
                            // =====================================================
                            .requestMatchers("/students/delete/**").hasRole("ADMIN")
                            .requestMatchers("/students/**").hasAnyRole("ADMIN", "INSTRUCTOR", "STUDENT")

                            // =====================================================
                            // 5. ENROLLMENTS (Admin & Instructor: Full CRUD, Student: View, Create/Enroll, Drop/Delete)
                            // =====================================================
                            .requestMatchers("/enrollments/edit").hasAnyRole("ADMIN", "INSTRUCTOR")
                            .requestMatchers("/enrollments/**").hasAnyRole("ADMIN", "INSTRUCTOR", "STUDENT")

                            // =====================================================
                            // 6. ATTENDANCE (Admin: Full, Instructor: View, Create, Update - NO Delete, Student: View Only)
                            // =====================================================
                            .requestMatchers("/attendance/delete").hasRole("ADMIN")
                            .requestMatchers("/attendance/new", "/attendance/edit", "/attendance/save").hasAnyRole("ADMIN", "INSTRUCTOR")
                            .requestMatchers("/attendance", "/attendance/").hasAnyRole("ADMIN", "INSTRUCTOR", "STUDENT")

                            // =====================================================
                            // 7. GRADEBOOKS (Admin: Full, Instructor: View, Create, Edit, Update - NO Delete, Student: View Only)
                            // =====================================================
                            .requestMatchers("/gradebooks/delete").hasRole("ADMIN")
                            .requestMatchers("/gradebooks/new", "/gradebooks/edit", "/gradebooks/save").hasAnyRole("ADMIN", "INSTRUCTOR")
                            .requestMatchers("/gradebooks", "/gradebooks/").hasAnyRole("ADMIN", "INSTRUCTOR", "STUDENT")

                            // --- Protected REST APIs ---
                            .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER", "INSTRUCTOR", "STUDENT")
                            .requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                            .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                            .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                            // --- Everything else requires authentication ---
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