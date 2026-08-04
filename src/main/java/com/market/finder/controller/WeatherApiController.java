package com.market.finder.controller;

import com.market.finder.entity.WeatherDto;
import com.market.finder.service.weather.WetherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

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
    public ResponseEntity<Map<String, Boolean>> getStatus() {
        return ResponseEntity.ok(Collections.singletonMap("enabled", wetherService.isEnabled()));
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Boolean>> toggleWeather(@RequestParam(name = "enabled") boolean enabled) {
        wetherService.setEnabled(enabled);
        return ResponseEntity.ok(Collections.singletonMap("enabled", wetherService.isEnabled()));
    }
}
