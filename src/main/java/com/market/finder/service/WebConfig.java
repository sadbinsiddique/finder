package com.market.finder.service;

import com.market.finder.security.CustomLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final CustomLoggingInterceptor customLoggingInterceptor;

    public WebConfig(CustomLoggingInterceptor customLoggingInterceptor) {
        this.customLoggingInterceptor = customLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customLoggingInterceptor)
                .addPathPatterns("/api/**");
    }
}