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
public class CustomLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingInterceptor.class);
    private final SecurityContextFacade securityContextFacade;

    public CustomLoggingInterceptor(SecurityContextFacade securityContextFacade) {
        this.securityContextFacade = securityContextFacade;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        String username = securityContextFacade.getCurrentUsername();

        logger.info("[preHandle] User='{}' IP={} {} {}",
                username, request.getRemoteAddr(), request.getMethod(), request.getRequestURI());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, @NonNull Object handler, Exception ex) {
        Object startTimeAttr = request.getAttribute("startTime");
        long duration = (startTimeAttr instanceof Long startTime) ? (System.currentTimeMillis() - startTime) : 0L;
        String username = securityContextFacade.getCurrentUsername();

        logger.info("[afterCompletion] User='{}' {} {} completed in {}ms (status={})",
                username, request.getMethod(), request.getRequestURI(), duration, response.getStatus());
    }
}
