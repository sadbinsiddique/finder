package com.market.finder.controller;

import com.market.finder.service.DashboardService;
import com.market.finder.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * SRP: Sole responsibility is rendering the Admin Dashboard & Permission listings.
 * DIP: Depends on DashboardService and PermissionService abstractions.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DashboardService dashboardService;
    private final PermissionService permissionService;

    public AdminController(DashboardService dashboardService,
                           PermissionService permissionService) {
        this.dashboardService = dashboardService;
        this.permissionService = permissionService;
    }

    // ===== Admin Dashboard =====
    @GetMapping
    public String showDashboard(Model model) {
        Map<String, Long> stats = dashboardService.getSystemStats();
        model.addAllAttributes(stats);
        return "admin/dashboard";
    }

    // ===== Permissions Management =====
    @GetMapping("/permissions")
    public String listPermissions(Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        return "admin/permissions/list";
    }
}
