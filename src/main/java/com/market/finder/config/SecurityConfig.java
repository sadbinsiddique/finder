package com.market.finder.config;

import com.market.finder.security.CachePersistentTokenRepository;
import com.market.finder.security.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CachePersistentTokenRepository persistentTokenRepository;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            CustomOAuth2UserService customOAuth2UserService,
            CachePersistentTokenRepository persistentTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public org.springframework.security.access.hierarchicalroles.RoleHierarchy roleHierarchy() {
        return org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl.fromHierarchy("""
                ROLE_ADMIN > MANAGE_USERS
                MANAGE_USERS > DELETE
                DELETE > WRITE
                WRITE > READ
                """);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        http.authenticationProvider(authenticationProvider(passwordEncoder));

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/login", "/register", "/forgot-password", "/reset-password", "/oauth2/**", "/login/oauth2/**", "/css/**", "/img/**", "/js/**").permitAll()
                        .requestMatchers("/error", "/access-denied").permitAll()
                        .requestMatchers("/admin/**", "/users/**", "/roles/**",
                        "/permissions/**").hasAnyAuthority("MANAGE_USERS", "ROLE_ADMIN")
                        .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        );

        http.rememberMe(remember -> remember
                .tokenRepository(persistentTokenRepository)
                .tokenValiditySeconds(86400 * 7)
                .key("finderRememberMeKey")
        );

        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                )
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID", "remember-me")
                .invalidateHttpSession(true)
                .permitAll()
        );

        http.exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
        );

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

