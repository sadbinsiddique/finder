package com.market.finder.controller;

import com.market.finder.entity.Role;
import com.market.finder.service.permission.PermissionService;
import com.market.finder.service.role.RoleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/roles")
@PreAuthorize("hasAnyAuthority('MANAGE_USERS', 'ROLE_ADMIN')")
public class AdminRoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    public AdminRoleController(
            RoleService roleService,
            PermissionService permissionService) {
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
        Role role = roleService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid role Id: " + id));

        model.addAttribute("role", role);
        model.addAttribute("allPermissions", permissionService.findAll());

        return "admin/roles/form";
    }

    @PostMapping("/save")
    public String saveRole(
            @Valid @ModelAttribute("role") Role role,
            BindingResult bindingResult,
            @RequestParam(value = "permissionIds", required = false) java.util.List<Integer> permissionIds,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allPermissions", permissionService.findAll());
            return "admin/roles/form";
        }
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
        try {
            if (roleService.existsById(id)) {
                roleService.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Role deleted successfully.");
            }
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/roles";
    }

}

