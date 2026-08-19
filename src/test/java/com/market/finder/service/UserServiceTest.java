package com.market.finder.service;

import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import com.market.finder.repository.InstructorRepository;
import com.market.finder.repository.StudentRepository;
import com.market.finder.repository.UserRepository;
import com.market.finder.service.role.RoleService;
import com.market.finder.service.user.UserServiceImpl;
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
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private InstructorRepository instructorRepository;

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
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleUser));

        Optional<User> userOpt = userService.findByUsername("testuser");

        assertTrue(userOpt.isPresent());
        assertEquals("testuser", userOpt.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testSaveUser_EncodesRawPassword() {
        when(passwordEncoder.encode("rawPassword")).thenReturn("$2a$10$encodedPasswordHash");
        when(userRepository.saveAndFlush(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.save(sampleUser);

        assertNotNull(savedUser);
        assertEquals("$2a$10$encodedPasswordHash", savedUser.getPassword());
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(userRepository, times(1)).saveAndFlush(sampleUser);
    }

    @Test
    void testDeleteByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleUser));
        doNothing().when(userRepository).deleteByUsername("testuser");

        userService.deleteByUsername("testuser");

        verify(userRepository, times(1)).deleteByUsername("testuser");
    }

    @Test
    void testRegisterNewUser_Success() {
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ROLE_USER");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(roleService.findByRoleName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("pass123")).thenReturn("$2a$10$encodedHash");
        when(userRepository.saveAndFlush(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User registered = userService.registerNewUser("newuser", "pass123", "ROLE_USER");

        assertNotNull(registered);
        assertEquals("newuser", registered.getUsername());
        assertEquals("$2a$10$encodedHash", registered.getPassword());
        assertTrue(registered.getRoles().contains(role));
    }

    @Test
    void testFindUserByIdentifier_DirectUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleUser));
        Optional<User> resolved = userService.findUserByIdentifier("testuser");
        assertTrue(resolved.isPresent());
        assertEquals("testuser", resolved.get().getUsername());
    }

    @Test
    void testFindUserByIdentifier_StudentEmail() {
        com.market.finder.entity.Student student = new com.market.finder.entity.Student();
        student.setUsername("student1");
        student.setEmail("alice.johnson@student.edu");

        when(userRepository.findByUsername("alice.johnson@student.edu")).thenReturn(Optional.empty());
        when(studentRepository.findByEmail("alice.johnson@student.edu")).thenReturn(Optional.of(student));
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(sampleUser));

        Optional<User> resolved = userService.findUserByIdentifier("alice.johnson@student.edu");
        assertTrue(resolved.isPresent());
    }

    @Test
    void testFindUserByIdentifier_EmailPrefixFallback() {
        when(userRepository.findByUsername("mdsiyam377@gmail.com")).thenReturn(Optional.empty());
        when(studentRepository.findByEmail("mdsiyam377@gmail.com")).thenReturn(Optional.empty());
        when(instructorRepository.findByEmail("mdsiyam377@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("mdsiyam377")).thenReturn(Optional.of(sampleUser));

        Optional<User> resolved = userService.findUserByIdentifier("mdsiyam377@gmail.com");
        assertTrue(resolved.isPresent());
    }

    @Test
    void testFindUserByIdentifier_DirectUserEmail() {
        when(userRepository.findByUsername("mdsiyam377@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("mdsiyam377@gmail.com")).thenReturn(Optional.of(sampleUser));

        Optional<User> resolved = userService.findUserByIdentifier("mdsiyam377@gmail.com");
        assertTrue(resolved.isPresent());
        assertEquals("testuser", resolved.get().getUsername());
    }

    @Test
    void testGetEmailByUsername_Student() {
        com.market.finder.entity.Student student = new com.market.finder.entity.Student();
        student.setEmail("alice.johnson@student.edu");
        when(studentRepository.findByUsername("student1")).thenReturn(Optional.of(student));

        String email = userService.getEmailByUsername("student1");
        assertEquals("alice.johnson@student.edu", email);
    }
}
