package com.market.finder.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CustomLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 10000);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            // 2. Pass the WRAPPERS down the chain, not the original request/response
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // 3. Extract the bodies
            String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
            String responseBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());

            // 4. Log everything!
            logger.info("API Request [{} {}] IP: {} | Request Body: {}",
                    request.getMethod(), request.getRequestURI(), request.getRemoteAddr(), requestBody);

            logger.info("API Response [Status: {} Time: {}ms] | Response Body: {}",
                    response.getStatus(), duration, responseBody);

            // 5. CRITICAL: You must copy the cached response body back to the actual response stream!
            // If you forget this line, the client (Postman/Browser) will get a blank response.
            responseWrapper.copyBodyToResponse();
        }
    }

    // Helper method to safely convert byte arrays to Strings
    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            if (contentAsByteArray == null || contentAsByteArray.length == 0) {
                return "[Empty]";
            }
            return new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            return "[Unsupported Encoding]";
        }
    }
}