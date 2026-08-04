package com.market.finder.controller;

import com.market.finder.entity.WeatherDto;
import com.market.finder.service.weather.WetherService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final WetherService wetherService;

    public HomeController(WetherService wetherService) {
        this.wetherService = wetherService;
    }

    @GetMapping
    public String showHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            model.addAttribute("username", auth.getName());
        }

        boolean enabled = wetherService.isEnabled();
        model.addAttribute("weatherEnabled", enabled);
        if (enabled) {
            WeatherDto weather = wetherService.getDefaultWeatherData();
            model.addAttribute("weather", weather);
        }

        return "index";
    }
}
