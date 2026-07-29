package com.market.finder.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SecurityContextFacadeImpl implements SecurityContextFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }

    @Override
    public String getCurrentUsername() {
        Authentication auth = getAuthentication();
        if (isAuthenticated()) {
            return auth.getName();
        }
        return "anonymous";
    }

    @Override
    public Set<String> getCurrentAuthorities() {
        Authentication auth = getAuthentication();
        if (auth == null) {
            return Collections.emptySet();
        } else {
            auth.getAuthorities();
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
