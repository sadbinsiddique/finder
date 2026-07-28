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
/**
 * SRP: Intercepts HTTP requests and enforces Dynamic Role & Permission-Based Access Control
 * evaluated dynamically against database-loaded user authorities.
 */
@Component
public class RoleAccessInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RoleAccessInterceptor.class);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return true; // Unauthenticated requests are handled by Spring Security filter chain
        }

        String uri = request.getRequestURI();
        String method = request.getMethod().toUpperCase();
        String username = auth.getName();
        Set<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        logger.info("[DYNAMIC-RBAC-INTERCEPTOR] User='{}' Authorities={} Method={} URI={}", username, authorities, method, uri);

        // ROLE_ADMIN override
        if (authorities.contains("ROLE_ADMIN")) {
            return true;
        }

        // 1. Admin / System Management paths (/admin/**, /users/**, /roles/**, /permissions/**)
        if (uri.startsWith("/admin") || uri.startsWith("/users") || uri.startsWith("/roles") || uri.startsWith("/permissions")) {
            if (!authorities.contains("MANAGE_USERS")) {
                return denyAccess(response, username, method, uri, "Requires MANAGE_USERS permission");
            }
            return true;
        }

        // 2. Delete actions (/delete or HTTP DELETE)
        if (uri.contains("/delete") || "DELETE".equals(method)) {
            if (!authorities.contains("DELETE")) {
                return denyAccess(response, username, method, uri, "Requires DELETE permission");
            }
        }
        // 3. Write actions (/new, /edit, /save or HTTP POST/PUT/PATCH)
        else if (uri.contains("/new") || uri.contains("/edit") || uri.contains("/save")
                || "POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) {
            if (!authorities.contains("WRITE")) {
                return denyAccess(response, username, method, uri, "Requires WRITE permission");
            }
        }
        // 4. Read actions (GET requests)
        else {
            if (!authorities.contains("READ")) {
                return denyAccess(response, username, method, uri, "Requires READ permission");
            }
        }

        return true;
    }

    private boolean denyAccess(HttpServletResponse response, String username, String method, String uri, String reason) throws Exception {
        logger.warn("[DYNAMIC-RBAC-DENIED] User='{}' Method={} URI={} Reason: {}", username, method, uri, reason);
        response.sendRedirect("/access-denied");
        return false;
    }
}


