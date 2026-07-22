package com.market.finder.controller;

import com.market.finder.entity.Role;
import com.market.finder.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * SRP: Sole responsibility is Admin Role Management.
 * DIP: Depends on RoleService interface.
 */
@Controller
@RequestMapping("/admin/roles")
public class AdminRoleController {

    private final RoleService roleService;

    public AdminRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/roles/list";
    }

    @GetMapping("/new")
    public String showCreateRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "admin/roles/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditRoleForm(@PathVariable Integer id, Model model) {
        Role role = roleService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id: " + id));
        model.addAttribute("role", role);
        return "admin/roles/form";
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") Role role) {
        roleService.save(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Integer id) {
        if (roleService.existsById(id)) {
            roleService.deleteById(id);
        }
        return "redirect:/admin/roles";
    }
}
