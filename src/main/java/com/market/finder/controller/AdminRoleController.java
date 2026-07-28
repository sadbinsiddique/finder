package com.market.finder.controller;

import com.market.finder.entity.Role;
import com.market.finder.service.PermissionService;
import com.market.finder.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * SRP: Sole responsibility is Admin Role Management & Dynamic Permission Assignments.
 * DIP: Depends on RoleService and PermissionService interfaces.
 */
@Controller
@RequestMapping("/admin/roles")
public class AdminRoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    public AdminRoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/roles/list";
    }

    @GetMapping("/new")
    public String showCreateRoleForm(Model model) {
        model.addAttribute("role", new Role());
        model.addAttribute("allPermissions", permissionService.findAll());
        return "admin/roles/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditRoleForm(@PathVariable Integer id, Model model) {
        Role role = roleService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id: " + id));
        model.addAttribute("role", role);
        model.addAttribute("allPermissions", permissionService.findAll());
        return "admin/roles/form";
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") Role role,
                           @RequestParam(value = "permissionIds", required = false) java.util.List<Integer> permissionIds) {
        if (permissionIds != null && !permissionIds.isEmpty()) {
            java.util.Set<com.market.finder.entity.Permission> selectedPermissions =
                    new java.util.HashSet<>(permissionService.findAllById(permissionIds));
            role.setPermissions(selectedPermissions);
        } else {
            role.setPermissions(new java.util.HashSet<>());
        }
        roleService.save(role);
        return "redirect:/admin/roles";
    }


    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Integer id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Role role = roleService.findById(id).orElse(null);
        if (role != null && "ROLE_ADMIN".equalsIgnoreCase(role.getRoleName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "The ROLE_ADMIN role cannot be deleted.");
            return "redirect:/admin/roles";
        }
        if (roleService.existsById(id)) {
            roleService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Role deleted successfully.");
        }
        return "redirect:/admin/roles";
    }

}

