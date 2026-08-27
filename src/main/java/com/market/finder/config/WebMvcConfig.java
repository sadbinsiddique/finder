package com.market.finder.config;

import com.market.finder.interceptor.AuthInterceptor;
import com.market.finder.interceptor.CustomLoggingInterceptor;
import com.market.finder.interceptor.RoleAccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CustomLoggingInterceptor customLoggingInterceptor;
    private final AuthInterceptor authInterceptor;
    private final RoleAccessInterceptor roleAccessInterceptor;

    public WebMvcConfig(
            CustomLoggingInterceptor customLoggingInterceptor,
            AuthInterceptor authInterceptor,
            RoleAccessInterceptor roleAccessInterceptor) {
        this.customLoggingInterceptor = customLoggingInterceptor;
        this.authInterceptor = authInterceptor;
        this.roleAccessInterceptor = roleAccessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customLoggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**",
                        "/img/**",
                        "/js/**",
                        "/login",
                        "/register",
                        "/forgot-password",
                        "/reset-password",
                        "/error"
                );

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**",
                        "/img/**",
                        "/js/**",
                        "/login",
                        "/register",
                        "/forgot-password",
                        "/reset-password",
                        "/error"
                );

        registry.addInterceptor(roleAccessInterceptor)
                .addPathPatterns(
                        "/admin/**",
                        "/users/**",
                        "/roles/**",
                        "/students/**",
                        "/instructors/**",
                        "/departments/**",
                        "/courses/**",
                        "/enrollments/**",
                        "/attendance/**",
                        "/gradebooks/**",
                        "/staff/**",
                        "/employees/**"
                );
    }
}

