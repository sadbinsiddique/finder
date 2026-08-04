package com.market.finder.security;

import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import com.market.finder.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");

        sampleUser = new User();
        sampleUser.setUsername("john");
        sampleUser.setPassword("secret");
        sampleUser.setEnabled(true);
        sampleUser.setRoles(Set.of(role));
    }

    @Test
    void loadUserByUsername_Success() {
        when(userService.findByUsername("john")).thenReturn(Optional.of(sampleUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("secret", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        verify(userService, times(1)).findByUsername("john");
    }

    @Test
    void loadUserByUsername_NotFound_ThrowsException() {
        when(userService.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown"));
        verify(userService, times(1)).findByUsername("unknown");
    }
}
