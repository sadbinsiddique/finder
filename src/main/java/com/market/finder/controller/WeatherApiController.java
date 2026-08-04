package com.market.finder.controller;

import com.market.finder.dto.WeatherDto;
import com.market.finder.service.WetherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/weather")
public class WeatherApiController {

    private final WetherService wetherService;

    public WeatherApiController(WetherService wetherService) {
        this.wetherService = wetherService;
    }

    @GetMapping
    public ResponseEntity<WeatherDto> getWeather(
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "lat", required = false) Double lat,
            @RequestParam(name = "lon", required = false) Double lon) {
        
        if (!wetherService.isEnabled()) {
            return ResponseEntity.ok(WeatherDto.error("Weather service is disabled"));
        }

        WeatherDto weather;
        if (lat != null && lon != null) {
            weather = wetherService.getWeatherDataByCoords(lat, lon);
        } else {
            weather = wetherService.getWeatherData(city);
        }
        return ResponseEntity.ok(weather);
    }

    @GetMapping("/status")
    public ResponseEntity<java.util.Map<String, Boolean>> getStatus() {
        return ResponseEntity.ok(java.util.Collections.singletonMap("enabled", wetherService.isEnabled()));
    }

    @org.springframework.web.bind.annotation.PostMapping("/toggle")
    public ResponseEntity<java.util.Map<String, Boolean>> toggleWeather(@RequestParam(name = "enabled") boolean enabled) {
        wetherService.setEnabled(enabled);
        return ResponseEntity.ok(java.util.Collections.singletonMap("enabled", wetherService.isEnabled()));
    }
}
