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
 * SRP: Intercepts Spring MVC HTTP requests and enforces fine-grained Role Access Control
 * before controller execution according to the exact Role Access Matrix.
 */
@Component
public class RoleAccessInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RoleAccessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return true; // Unauthenticated requests are handled by Spring Security filter chain
        }

        String uri = request.getRequestURI();
        String method = request.getMethod().toUpperCase();
        String username = auth.getName();
        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        boolean isAdmin = roles.contains("ROLE_ADMIN");
        boolean isInstructor = roles.contains("ROLE_INSTRUCTOR");
        boolean isStudent = roles.contains("ROLE_STUDENT");

        logger.info("[ROLE-INTERCEPTOR] Checking User='{}' Roles={} Method={} URI={}", username, roles, method, uri);

        // 1. ADMIN - Full access to all endpoints
        if (isAdmin) {
            return true;
        }

        // 2. Admin-only endpoints (/admin/**, /users/**, /roles/**)
        if (uri.startsWith("/admin") || uri.startsWith("/users") || uri.startsWith("/roles")) {
            return denyAccess(response, username, method, uri, "Admin path restricted to ADMIN role only");
        }

        // 3. /instructors module (Admin: Full, Student: View Only, Instructor: NO ACCESS)
        if (uri.startsWith("/instructors")) {
            if (isInstructor) {
                return denyAccess(response, username, method, uri, "INSTRUCTOR role has no access to /instructors module");
            }
            if (isStudent && !("GET".equals(method) && isViewPath(uri))) {
                return denyAccess(response, username, method, uri, "STUDENT role can only view /instructors");
            }
        }

        // 4. /courses module (Admin: Full, Student: View Only, Instructor: NO ACCESS)
        if (uri.startsWith("/courses")) {
            if (isInstructor) {
                return denyAccess(response, username, method, uri, "INSTRUCTOR role has no access to /courses module");
            }
            if (isStudent && !("GET".equals(method) && isViewPath(uri))) {
                return denyAccess(response, username, method, uri, "STUDENT role can only view /courses");
            }
        }

        // 5. /departments module (Admin: Full, Instructor: View & Update, Student: View Only)
        if (uri.startsWith("/departments")) {
            if (uri.contains("/delete") || uri.contains("/new")) {
                if (!isAdmin) {
                    return denyAccess(response, username, method, uri, "Department create/delete restricted to ADMIN role");
                }
            }
            if (isStudent && (uri.contains("/edit") || uri.contains("/save"))) {
                return denyAccess(response, username, method, uri, "STUDENT role can only view /departments");
            }
        }

        // 6. /students module (Admin: Full, Instructor & Student: View, Create, Edit - NO Delete)
        if (uri.startsWith("/students")) {
            if (uri.contains("/delete")) {
                if (!isAdmin) {
                    return denyAccess(response, username, method, uri, "Student deletion restricted to ADMIN role");
                }
            }
        }

        // 7. /attendance module (Admin: Full, Instructor: View, Create, Update - NO Delete, Student: View Only)
        if (uri.startsWith("/attendance")) {
            if (uri.contains("/delete")) {
                if (!isAdmin) {
                    return denyAccess(response, username, method, uri, "Attendance deletion restricted to ADMIN role");
                }
            }
            if (isStudent && (uri.contains("/new") || uri.contains("/edit") || uri.contains("/save"))) {
                return denyAccess(response, username, method, uri, "STUDENT role can only view /attendance");
            }
        }

        // 8. /gradebooks module (Admin: Full, Instructor: View, Create, Edit, Update - NO Delete, Student: View Only)
        if (uri.startsWith("/gradebooks")) {
            if (uri.contains("/delete")) {
                if (!isAdmin) {
                    return denyAccess(response, username, method, uri, "Gradebook deletion restricted to ADMIN role");
                }
            }
            if (isStudent && (uri.contains("/new") || uri.contains("/edit") || uri.contains("/save"))) {
                return denyAccess(response, username, method, uri, "STUDENT role can only view /gradebooks");
            }
        }

        // 9. /enrollments module (Admin & Instructor: Full, Student: View, Create/Enroll, Drop/Delete - NO Edit)
        if (uri.startsWith("/enrollments")) {
            if (isStudent && uri.contains("/edit")) {
                return denyAccess(response, username, method, uri, "STUDENT role cannot edit existing enrollments");
            }
        }

        return true;
    }

    private boolean isViewPath(String uri) {
        return uri.equals("/instructors") || uri.equals("/instructors/")
                || uri.equals("/courses") || uri.equals("/courses/");
    }

    private boolean denyAccess(HttpServletResponse response, String username, String method, String uri, String reason) throws Exception {
        logger.warn("[ROLE-INTERCEPTOR-DENIED] User='{}' Method={} URI={} Reason: {}", username, method, uri, reason);
        response.sendRedirect("/access-denied");
        return false;
    }
}
