package com.market.finder.security;

import com.market.finder.dao.UserRepository;
import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Loads user details directly from MySQL database for Spring Security authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        logger.info("[AUTH-QUERY] Querying MySQL database for user: '{}'", username);

        User user = userRepository.findById(username)
                .orElseThrow(() -> {
                    logger.warn("[AUTH-QUERY-FAILED] User '{}' not found in MySQL database", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        boolean isEnabled = Boolean.TRUE.equals(user.getEnabled());
        logger.info("[AUTH-QUERY-SUCCESS] Found user '{}' in MySQL database. Enabled={}, RolesCount={}",
                user.getUsername(), isEnabled, user.getRoles() != null ? user.getRoles().size() : 0);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                isEnabled,
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                mapRolesToAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        if (roles == null) return java.util.Collections.emptyList();
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }
}
