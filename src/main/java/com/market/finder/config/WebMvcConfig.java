package com.market.finder.config;

import com.market.finder.interceptor.RoleAccessInterceptor;
import com.market.finder.security.CustomLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SRP: This class's sole responsibility is MVC interceptor registration.
 * Registers RoleAccessInterceptor to enforce pre-controller role security.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CustomLoggingInterceptor customLoggingInterceptor;
    private final RoleAccessInterceptor roleAccessInterceptor;

    public WebMvcConfig(CustomLoggingInterceptor customLoggingInterceptor,
                        RoleAccessInterceptor roleAccessInterceptor) {
        this.customLoggingInterceptor = customLoggingInterceptor;
        this.roleAccessInterceptor = roleAccessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Logging interceptor for all requests
        registry.addInterceptor(customLoggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/img/**", "/js/**", "/login", "/error");

        // Role access control interceptor for protected modules
        registry.addInterceptor(roleAccessInterceptor)
                .addPathPatterns("/admin/**", "/users/**", "/roles/**",
                        "/students/**", "/instructors/**", "/departments/**",
                        "/courses/**", "/enrollments/**", "/attendance/**", "/gradebooks/**");
    }
}
