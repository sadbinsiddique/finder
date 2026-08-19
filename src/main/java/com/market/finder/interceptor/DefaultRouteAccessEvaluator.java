package com.market.finder.interceptor;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class DefaultRouteAccessEvaluator implements RouteAccessEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRouteAccessEvaluator.class);

    @Override
    public boolean evaluate(HttpServletResponse response, String username, String method, String uri, Set<String> authorities) throws IOException {
        boolean isAdminOrManager = authorities.contains("ROLE_ADMIN") || authorities.contains("MANAGE_USERS");
        boolean canDelete = isAdminOrManager || authorities.contains("DELETE");
        boolean canWrite = canDelete || authorities.contains("WRITE");
        boolean canRead = canWrite || authorities.contains("READ");

        if (isAdminEndpoint(uri)) {
            if (!isAdminOrManager) {
                return denyAccess(response, username, method, uri, "Requires MANAGE_USERS permission");
            }
            return true;
        }

        if (isDeleteOperation(uri, method)) {
            if (!canDelete) {
                return denyAccess(response, username, method, uri, "Requires DELETE permission");
            }
        } else if (isWriteOperation(uri, method)) {
            if (!canWrite) {
                return denyAccess(response, username, method, uri, "Requires WRITE permission");
            }
        } else {
            if (!canRead) {
                return denyAccess(response, username, method, uri, "Requires READ permission");
            }
        }

        return true;
    }

    private boolean isAdminEndpoint(String uri) {
        return uri.startsWith("/admin") || uri.startsWith("/users") || uri.startsWith("/roles") || uri.startsWith("/permissions");
    }

    private boolean isDeleteOperation(String uri, String method) {
        return uri.contains("/delete") || "DELETE".equals(method);
    }

    private boolean isWriteOperation(String uri, String method) {
        return uri.contains("/new") || uri.contains("/edit") || uri.contains("/save") || "POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method);
    }

    private boolean denyAccess(HttpServletResponse response, String username, String method, String uri, String reason) throws IOException {
        logger.warn("[RBAC-DENIED] User='{}' Method={} URI={} Reason: {}", username, method, uri, reason);
        response.sendRedirect("/access-denied");
        return false;
    }
}
