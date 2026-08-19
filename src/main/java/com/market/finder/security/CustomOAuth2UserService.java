package com.market.finder.security;

import com.market.finder.entity.Permission;
import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import com.market.finder.service.role.RoleService;
import com.market.finder.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    private final UserService userService;
    private final RoleService roleService;

    public CustomOAuth2UserService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        return processOAuth2User(registrationId, oauth2User);
    }

    public OAuth2User processOAuth2User(String registrationId, OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        String username = extractUsername(registrationId, attributes);

        logger.info("[OAUTH2-LOGIN] Processing OAuth2 login for provider='{}', username='{}'", registrationId, username);

        User user = userService.findByUsername(username)
                .orElseGet(() -> autoProvisionUser(username, registrationId));

        Set<GrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());
        
        // Retain any provider authorities if present
        authorities.addAll(oauth2User.getAuthorities());

        String nameAttributeKey = getAttributeKeyForProvider(registrationId, attributes);

        return new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
    }

    private String extractUsername(String provider, Map<String, Object> attributes) {
        if ("github".equalsIgnoreCase(provider)) {
            String login = (String) attributes.get("login");
            if (login != null && !login.isBlank()) {
                return login;
            }
        }
        
        String email = (String) attributes.get("email");
        if (email != null && !email.isBlank()) {
            return email;
        }

        String sub = (String) attributes.get("sub");
        if (sub != null && !sub.isBlank()) {
            return sub;
        }

        return (String) attributes.getOrDefault("id", "oauth2_user_" + System.currentTimeMillis());
    }

    private User autoProvisionUser(String username, String provider) {
        logger.info("[OAUTH2-PROVISION] Auto-registering new user from OAuth2 provider='{}': '{}'", provider, username);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEnabled(true);
        newUser.setPassword("{noop}OAUTH2_AUTHENTICATED_" + UUID.randomUUID());

        Set<Role> roles = new HashSet<>();
        roleService.findByRoleName("ROLE_USER").ifPresent(roles::add);
        newUser.setRoles(roles);

        return userService.save(newUser);
    }

    private Set<GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        if (roles == null) return Collections.emptySet();
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

    private String getAttributeKeyForProvider(String provider, Map<String, Object> attributes) {
        if ("github".equalsIgnoreCase(provider) && attributes.containsKey("login")) {
            return "login";
        }
        if (attributes.containsKey("email")) {
            return "email";
        }
        if (attributes.containsKey("sub")) {
            return "sub";
        }
        return "name";
    }
}
