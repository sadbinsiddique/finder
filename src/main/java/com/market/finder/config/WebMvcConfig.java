package com.market.finder.config;

import com.market.finder.security.AuthInterceptor;
import com.market.finder.security.CustomLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SRP: This class's sole responsibility is MVC interceptor registration.
 * Moved from service package to config package where it belongs.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CustomLoggingInterceptor customLoggingInterceptor;
    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(CustomLoggingInterceptor customLoggingInterceptor,
                        AuthInterceptor authInterceptor) {
        this.customLoggingInterceptor = customLoggingInterceptor;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Logging interceptor for all requests
        registry.addInterceptor(customLoggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/img/**", "/js/**", "/login", "/error");

        // Auth interceptor for protected pages
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**", "/users/**", "/roles/**",
                        "/students/**", "/instructors/**", "/departments/**",
                        "/courses/**", "/enrollments/**", "/attendance/**", "/gradebooks/**");
    }
}
