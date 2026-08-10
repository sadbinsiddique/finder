package com.market.finder.interceptor;

import com.market.finder.security.SecurityContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class RoleAccessInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RoleAccessInterceptor.class);
    private final SecurityContextFacade securityContextFacade;
    private final RouteAccessEvaluator routeAccessEvaluator;

    public RoleAccessInterceptor(SecurityContextFacade securityContextFacade, RouteAccessEvaluator routeAccessEvaluator) {
        this.securityContextFacade = securityContextFacade;
        this.routeAccessEvaluator = routeAccessEvaluator;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        if (!securityContextFacade.isAuthenticated()) {
            return true;
        }

        String uri = request.getRequestURI();
        String method = request.getMethod().toUpperCase();
        String username = securityContextFacade.getCurrentUsername();
        Set<String> authorities = securityContextFacade.getCurrentAuthorities();

        logger.info("[INTERCEPTOR] User='{}' Authorities={} Method={} URI={}", username, authorities, method, uri);

        if (authorities.contains("ROLE_ADMIN")) {
            return true;
        }

        return routeAccessEvaluator.evaluate(response, username, method, uri, authorities);
    }
}

