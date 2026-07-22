package com.market.finder.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        String method = request.getMethod();

        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {

            String username = auth.getName();
            String roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));

            logger.info("[AUTH] User='{}' Roles=[{}] IP={} {} {}",
                    username, roles, ip, method, uri);

            // Check admin path access
            if (uri.startsWith("/admin")) {
                boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));
                if (!isAdmin) {
                    logger.warn("[AUTH-DENIED] User='{}' attempted admin access: {} {}",
                            username, method, uri);
                    response.sendRedirect("/access-denied");
                    return false;
                }
            }
        } else {
            logger.info("[AUTH] Anonymous request: IP={} {} {}", ip, method, uri);
        }

        return true;
    }
}
