package com.market.finder.interceptor;

import com.market.finder.security.SecurityContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private final SecurityContextFacade securityContextFacade;

    public AuthInterceptor(SecurityContextFacade securityContextFacade) {
        this.securityContextFacade = securityContextFacade;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        String method = request.getMethod();

        if (securityContextFacade.isAuthenticated()) {
            String username = securityContextFacade.getCurrentUsername();
            String roles = String.join(", ", securityContextFacade.getCurrentAuthorities());
            logger.info("[AUTH] User='{}' Authorities=[{}] IP={} {} {}", username, roles, ip, method, uri);
        } else {
            logger.info("[AUTH] Anonymous request: IP={} {} {}", ip, method, uri);
        }

        return true;
    }
}
