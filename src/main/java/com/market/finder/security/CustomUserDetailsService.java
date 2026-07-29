package com.market.finder.security;

import com.market.finder.entity.Permission;
import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import com.market.finder.service.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        boolean isEnabled = Boolean.TRUE.equals(user.getEnabled());

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
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            if (role.getRoleName() != null) {
                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            }
            if (role.getPermissions() != null) {
                for (Permission perm : role.getPermissions()) {
                    if (perm.getPermissionName() != null) {
                        authorities.add(new SimpleGrantedAuthority(perm.getPermissionName()));
                    }
                }
            }
        }
        return authorities;
    }
}
