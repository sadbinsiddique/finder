package com.market.finder.interceptor;

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

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleAccessInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RoleAccessInterceptor.class);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return true;
        }

        String uri = request.getRequestURI();
        String method = request.getMethod().toUpperCase();
        String username = auth.getName();
        Set<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        logger.info("[INTERCEPTOR] User='{}' Authorities={} Method={} URI={}", username, authorities, method, uri);

        if (authorities.contains("ROLE_ADMIN")) {
            return true;
        }

        if (uri.startsWith("/admin") || uri.startsWith("/users") || uri.startsWith("/roles") || uri.startsWith("/permissions")) {
            if (!authorities.contains("MANAGE_USERS")) {
                return denyAccess(response, username, method, uri, "Requires MANAGE_USERS permission");
            }
            return true;
        }

        if (uri.contains("/delete") || "DELETE".equals(method)) {
            if (!authorities.contains("DELETE")) {
                return denyAccess(response, username, method, uri, "Requires DELETE permission");
            }
        }

        else if (uri.contains("/new") || uri.contains("/edit") || uri.contains("/save")
                || "POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) {
            if (!authorities.contains("WRITE")) {
                return denyAccess(response, username, method, uri, "Requires WRITE permission");
            }
        }

        else {
            if (!authorities.contains("READ")) {
                return denyAccess(response, username, method, uri, "Requires READ permission");
            }
        }

        return true;
    }

    private boolean denyAccess(HttpServletResponse response, String username, String method, String uri, String reason) throws Exception {
        logger.warn("[RBAC-DENIED] User='{}' Method={} URI={} Reason: {}", username, method, uri, reason);
        response.sendRedirect("/access-denied");
        return false;
    }
}


