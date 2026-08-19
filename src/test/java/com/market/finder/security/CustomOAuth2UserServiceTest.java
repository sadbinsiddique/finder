package com.market.finder.security;

import com.market.finder.entity.Permission;
import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import com.market.finder.service.role.RoleService;
import com.market.finder.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    private User existingUser;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        adminRole = new Role();
        adminRole.setRoleName("ROLE_ADMIN");
        adminRole.setPermissions(Set.of(new Permission("MANAGE_USERS")));

        existingUser = new User();
        existingUser.setUsername("octocat");
        existingUser.setEnabled(true);
        existingUser.setRoles(Set.of(adminRole));
    }

    @Test
    void testProcessOAuth2User_ExistingUser() {
        OAuth2User mockOAuth2User = mock(OAuth2User.class);
        when(mockOAuth2User.getAttributes()).thenReturn(Map.of("login", "octocat", "name", "The Octocat"));
        when(mockOAuth2User.getAuthorities()).thenReturn(Set.of());
        when(userService.findByUsername("octocat")).thenReturn(Optional.of(existingUser));

        OAuth2User result = customOAuth2UserService.processOAuth2User("github", mockOAuth2User);

        assertNotNull(result);
        assertEquals("octocat", result.getName());
        assertTrue(result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN")));
        assertTrue(result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("MANAGE_USERS")));

        verify(userService, never()).save(any());
    }

    @Test
    void testProcessOAuth2User_NewUserAutoProvisioning() {
        OAuth2User mockOAuth2User = mock(OAuth2User.class);
        when(mockOAuth2User.getAttributes()).thenReturn(Map.of("login", "newdev", "email", "newdev@github.com"));
        when(mockOAuth2User.getAuthorities()).thenReturn(Set.of());
        when(userService.findByUsername("newdev")).thenReturn(Optional.empty());

        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        userRole.setPermissions(Set.of(new Permission("READ")));
        when(roleService.findByRoleName("ROLE_USER")).thenReturn(Optional.of(userRole));

        User provisionedUser = new User();
        provisionedUser.setUsername("newdev");
        provisionedUser.setEnabled(true);
        provisionedUser.setRoles(Set.of(userRole));
        when(userService.save(any(User.class))).thenReturn(provisionedUser);

        OAuth2User result = customOAuth2UserService.processOAuth2User("github", mockOAuth2User);

        assertNotNull(result);
        assertEquals("newdev", result.getName());
        assertTrue(result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_USER")));
        assertTrue(result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("READ")));

        verify(userService, times(1)).save(any(User.class));
    }
}
