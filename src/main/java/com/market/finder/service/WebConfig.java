package com.market.finder.service;

import com.market.finder.security.AuthInterceptor;
import com.market.finder.security.CustomLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CustomLoggingInterceptor customLoggingInterceptor;
    private final AuthInterceptor authInterceptor;

    public WebConfig(CustomLoggingInterceptor customLoggingInterceptor,
                     AuthInterceptor authInterceptor) {
        this.customLoggingInterceptor = customLoggingInterceptor;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Logging interceptor for all API and page requests
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