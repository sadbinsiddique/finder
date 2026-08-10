package com.market.finder.interceptor;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public interface RouteAccessEvaluator {
    boolean evaluate(HttpServletResponse response, String username, String method, String uri, Set<String> authorities) throws IOException;
}
