package com.market.finder.security;

import org.springframework.security.core.Authentication;

import java.util.Set;

public interface SecurityContextFacade {
    Authentication getAuthentication();
    boolean isAuthenticated();
    String getCurrentUsername();
    Set<String> getCurrentAuthorities();
}
