package com.market.finder.controller;

import com.market.finder.entity.WeatherDto;
import com.market.finder.service.dashboard.DashboardService;
import com.market.finder.service.permission.PermissionService;
import com.market.finder.service.weather.WetherService;
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
    private final WetherService wetherService;

    public AdminController(DashboardService dashboardService, PermissionService permissionService, WetherService wetherService) {
        this.dashboardService = dashboardService;
        this.permissionService = permissionService;
        this.wetherService = wetherService;
    }

    @GetMapping
    public String showDashboard(Model model) {
        Map<String, Long> stats = dashboardService.getSystemStats();
        model.addAllAttributes(stats);
        model.addAttribute("userCount", stats.getOrDefault("totalUsers", 0L));
        model.addAttribute("roleCount", stats.getOrDefault("totalRoles", 0L));
        model.addAttribute("studentCount", stats.getOrDefault("totalStudents", 0L));
        
        boolean enabled = wetherService.isEnabled();
        model.addAttribute("weatherEnabled", enabled);
        if (enabled) {
            WeatherDto weather = wetherService.getDefaultWeatherData();
            model.addAttribute("weather", weather);
        }

        return "admin/dashboard";
    }

    @GetMapping("/permissions")
    public String listPermissions(Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        return "admin/permissions/list";
    }
}
