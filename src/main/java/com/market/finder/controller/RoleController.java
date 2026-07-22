package com.market.finder.controller;

import com.market.finder.dao.RoleRepository;
import com.market.finder.entity.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // 1. Show all roles
    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "roles/list";
    }

    // 2. Show the form to create a new role
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/form";
    }

    // 3. Show the form to edit an existing role
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id: " + id));
        model.addAttribute("role", role);
        return "roles/form";
    }

    // 4. Save the role (Handles both Create and Update)
    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") Role role) {
        roleRepository.save(role);
        return "redirect:/roles";
    }

    // 5. Delete a role
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Integer id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
        }
        return "redirect:/roles";
    }
}