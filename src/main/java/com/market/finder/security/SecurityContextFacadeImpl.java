package com.market.finder.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
        if (isAuthenticated() && auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                return userDetails.getUsername();
            } else if (principal instanceof OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                if (email != null && !email.isBlank()) {
                    return email;
                }
                String login = oauth2User.getAttribute("login");
                if (login != null && !login.isBlank()) {
                    return login;
                }
                return oauth2User.getName();
            }
            return auth.getName();
        }
        return "anonymous";
    }

    @Override
    public Set<String> getCurrentAuthorities() {
        Authentication auth = getAuthentication();
        if (auth == null) {
            return Collections.emptySet();
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasAuthority(String authority) {
        return getCurrentAuthorities().contains(authority);
    }

    @Override
    public boolean isAdmin() {
        return hasAuthority("ROLE_ADMIN");
    }
}

