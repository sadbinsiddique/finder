package com.market.finder.service;

import com.market.finder.dao.RoleRepository;
import com.market.finder.dao.UserRepository;
import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUsername("testuser");
        sampleUser.setPassword("rawPassword");
        sampleUser.setEnabled(true);
    }

    @Test
    void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<User> users = userService.findAll();

        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByUsername_Success() {
        when(userRepository.findById("testuser")).thenReturn(Optional.of(sampleUser));

        Optional<User> userOpt = userService.findByUsername("testuser");

        assertTrue(userOpt.isPresent());
        assertEquals("testuser", userOpt.get().getUsername());
        verify(userRepository, times(1)).findById("testuser");
    }

    @Test
    void testSaveUser_EncodesRawPassword() {
        when(passwordEncoder.encode("rawPassword")).thenReturn("$2a$10$encodedPasswordHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.save(sampleUser);

        assertNotNull(savedUser);
        assertEquals("$2a$10$encodedPasswordHash", savedUser.getPassword());
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testDeleteByUsername() {
        doNothing().when(userRepository).deleteById("testuser");

        userService.deleteByUsername("testuser");

        verify(userRepository, times(1)).deleteById("testuser");
    }

    @Test
    void testRegisterNewUser_Success() {
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ROLE_USER");

        when(userRepository.findById("newuser")).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("pass123")).thenReturn("$2a$10$encodedHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User registered = userService.registerNewUser("newuser", "pass123", "ROLE_USER");

        assertNotNull(registered);
        assertEquals("newuser", registered.getUsername());
        assertEquals("$2a$10$encodedHash", registered.getPassword());
        assertTrue(registered.getRoles().contains(role));
    }
}
