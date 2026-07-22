package com.market.finder.controller;

import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import com.market.finder.service.RoleService;
import com.market.finder.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private Model model;

    @InjectMocks
    private AdminUserController adminUserController;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUsername("testadmin");
        sampleUser.setPassword("password123");
        sampleUser.setEnabled(true);
    }

    @Test
    void testListUsers() {
        when(userService.findAll()).thenReturn(List.of(sampleUser));

        String view = adminUserController.listUsers(model);

        assertEquals("admin/users/list", view);
        verify(model, times(1)).addAttribute("users", List.of(sampleUser));
    }

    @Test
    void testShowCreateUserForm() {
        when(roleService.findAll()).thenReturn(List.of());

        String view = adminUserController.showCreateUserForm(model);

        assertEquals("admin/users/form", view);
        verify(model, times(1)).addAttribute(eq("user"), any(User.class));
        verify(model, times(1)).addAttribute("allRoles", List.of());
    }

    @Test
    void testShowEditUserForm_Success() {
        when(userService.findByUsername("testadmin")).thenReturn(Optional.of(sampleUser));
        when(roleService.findAll()).thenReturn(List.of());

        String view = adminUserController.showEditUserForm("testadmin", model);

        assertEquals("admin/users/form", view);
        verify(model, times(1)).addAttribute("user", sampleUser);
    }

    @Test
    void testSaveUser_PreservesPasswordWhenEmpty() {
        User editUser = new User();
        editUser.setUsername("testadmin");
        editUser.setPassword(""); // Empty password submission

        when(userService.findByUsername("testadmin")).thenReturn(Optional.of(sampleUser));
        when(userService.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        String view = adminUserController.saveUser(editUser, List.of());

        assertEquals("redirect:/admin/users", view);
        assertEquals("password123", editUser.getPassword()); // Preserved existing password!
        verify(userService, times(1)).save(editUser);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteByUsername("testadmin");

        String view = adminUserController.deleteUser("testadmin");

        assertEquals("redirect:/admin/users", view);
        verify(userService, times(1)).deleteByUsername("testadmin");
    }
}
