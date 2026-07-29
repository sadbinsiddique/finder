package com.market.finder.controller;

import com.market.finder.service.DashboardService;
import com.market.finder.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DashboardService dashboardService;
    private final PermissionService permissionService;

    public AdminController(DashboardService dashboardService, PermissionService permissionService) {
        this.dashboardService = dashboardService;
        this.permissionService = permissionService;
    }

    @GetMapping
    public String showDashboard(Model model) {
        Map<String, Long> stats = dashboardService.getSystemStats();
        model.addAllAttributes(stats);
        model.addAttribute("userCount", stats.getOrDefault("totalUsers", 0L));
        model.addAttribute("roleCount", stats.getOrDefault("totalRoles", 0L));
        model.addAttribute("studentCount", stats.getOrDefault("totalStudents", 0L));
        return "admin/dashboard";
    }

    @GetMapping("/permissions")
    public String listPermissions(Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        return "admin/permissions/list";
    }
}
