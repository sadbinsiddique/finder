package com.market.finder.controller;

import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import com.market.finder.service.RoleService;
import com.market.finder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SRP: Sole responsibility is Admin User Management.
 * DIP: Depends on UserService and RoleService interfaces.
 */
@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminUserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users/list";
    }

    @GetMapping("/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/users/form";
    }

    @GetMapping("/edit/{username}")
    public String showEditUserForm(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/users/form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "roleIds", required = false) List<Integer> roleIds) {
        
        // Preserve existing password if editing user and password field was left empty
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
            userService.findByUsername(user.getUsername()).ifPresent(existingUser -> {
                if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                    user.setPassword(existingUser.getPassword());
                }
            });
        }

        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> selectedRoles = new HashSet<>(roleService.findAllById(roleIds));
            user.setRoles(selectedRoles);
        } else {
            user.setRoles(new HashSet<>());
        }

        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return "redirect:/admin/users";
    }
}
