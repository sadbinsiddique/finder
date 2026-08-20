package com.market.finder.controller;

import com.market.finder.dto.WeatherDto;
import com.market.finder.service.dashboard.DashboardService;
import com.market.finder.service.permission.PermissionService;
import com.market.finder.service.weather.WeatherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('MANAGE_USERS', 'ROLE_ADMIN')")
public class AdminController {

    private final DashboardService dashboardService;
    private final PermissionService permissionService;
    private final WeatherService weatherService;

    public AdminController(DashboardService dashboardService, PermissionService permissionService, WeatherService weatherService) {
        this.dashboardService = dashboardService;
        this.permissionService = permissionService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public String showDashboard(Model model) {

        Map<String, Long> stats = dashboardService.getSystemStats();

        model.addAllAttributes(stats);
        model.addAttribute("userCount", stats.getOrDefault("totalUsers", 0L));
        model.addAttribute("roleCount", stats.getOrDefault("totalRoles", 0L));
        model.addAttribute("studentCount", stats.getOrDefault("totalStudents", 0L));
        
        boolean enabled = weatherService.isEnabled();

        model.addAttribute("weatherEnabled", enabled);
        if (enabled) {
            WeatherDto weather = weatherService.getDefaultWeatherData();
            model.addAttribute("weather", weather);
        }
        return "admin/dashboard";
    }

    @GetMapping("/permissions")
    public String listPermissions(Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        return "admin/permissions/list";
    }

    @GetMapping("/settings")
    public String showSettings(Model model) {
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("osName", System.getProperty("os.name"));
        model.addAttribute("weatherEnabled", weatherService.isEnabled());
        return "admin/settings";
    }

    @GetMapping("/settings/general")
    public String showGeneralSettings(Model model) {
        model.addAttribute("adminEmail", "vitalglowlifestyle@gmail.com");
        model.addAttribute("timezone", "Asia/Dhaka");
        model.addAttribute("direction", "ltr");
        model.addAttribute("siteLanguage", "en");
        model.addAttribute("errorReporting", true);
        model.addAttribute("redirectNotFound", true);
        model.addAttribute("auditLogRetention", "1_year");
        model.addAttribute("licenseStatus", "Licensed to vitalglowlifestyle. Activated since Mar 14 2026.");
        return "admin/settings-general";
    }

    @org.springframework.web.bind.annotation.PostMapping("/settings/general")
    public String saveGeneralSettings(
            @org.springframework.web.bind.annotation.RequestParam(name = "adminEmail", required = false) String adminEmail,
            @org.springframework.web.bind.annotation.RequestParam(name = "timezone", required = false) String timezone,
            @org.springframework.web.bind.annotation.RequestParam(name = "direction", required = false) String direction,
            @org.springframework.web.bind.annotation.RequestParam(name = "siteLanguage", required = false) String siteLanguage,
            @org.springframework.web.bind.annotation.RequestParam(name = "errorReporting", defaultValue = "false") boolean errorReporting,
            @org.springframework.web.bind.annotation.RequestParam(name = "redirectNotFound", defaultValue = "false") boolean redirectNotFound,
            @org.springframework.web.bind.annotation.RequestParam(name = "auditLogRetention", defaultValue = "1_year") String auditLogRetention,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        
        redirectAttributes.addFlashAttribute("savedSuccess", true);
        return "redirect:/admin/settings/general?success=true";
    }
}
