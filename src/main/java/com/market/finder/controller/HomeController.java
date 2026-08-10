package com.market.finder.controller;

import com.market.finder.dto.WeatherDto;
import com.market.finder.security.SecurityContextFacade;
import com.market.finder.service.weather.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final WeatherService weatherService;
    private final SecurityContextFacade securityContextFacade;

    public HomeController(WeatherService weatherService, SecurityContextFacade securityContextFacade) {
        this.weatherService = weatherService;
        this.securityContextFacade = securityContextFacade;
    }

    @GetMapping
    public String showHome(Model model) {
        if (securityContextFacade.isAuthenticated()) {
            model.addAttribute("username", securityContextFacade.getCurrentUsername());
        }

        boolean enabled = weatherService.isEnabled();
        model.addAttribute("weatherEnabled", enabled);
        if (enabled) {
            WeatherDto weather = weatherService.getDefaultWeatherData();
            model.addAttribute("weather", weather);
        }

        return "index";
    }
}


