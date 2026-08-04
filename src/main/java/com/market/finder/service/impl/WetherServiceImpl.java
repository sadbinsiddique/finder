package com.market.finder.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.market.finder.dto.WeatherDto;
import com.market.finder.service.WetherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WetherServiceImpl implements WetherService {

    private static final Logger logger = LoggerFactory.getLogger(WetherServiceImpl.class);

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url:https://api.openweathermap.org/data/2.5/weather}")
    private String apiUrl;

    @Value("${weather.api.default-city:London}")
    private String defaultCity;

    private volatile boolean enabled = false;

    }

    @Override
    @Cacheable(value = "weatherCity", key = "#city != null ? #city : 'default'", unless = "#result == null || !#result.isSuccess()")
    public WeatherDto getWeatherData(String city) {
        String targetCity = (city == null || city.trim().isEmpty()) ? defaultCity : city.trim();

        try {
            }
        } catch (Exception e) {
            logger.error("Error fetching weather data for city {}: {}", targetCity, e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "weatherCoords", key = "T(java.lang.String).format('%.2f_%.2f', #lat, #lon)", unless = "#result == null || !#result.isSuccess()")
    public WeatherDto getWeatherDataByCoords(double lat, double lon) {
        String locationLabel = geoLocationService.resolveLocationName(lat, lon);

        try {
            }
        } catch (Exception e) {
            logger.error("Error fetching weather data for coords ({}, {}): {}", lat, lon, e.getMessage());
        }
    }

    private WeatherDto getWeatherDto(JsonNode root, WeatherDto dto) {
        if (root.has("sys")) {
            dto.setCountry(root.path("sys").path("country").asText(""));
        }

        if (root.has("main")) {
            JsonNode main = root.path("main");
            dto.setTemperature(main.path("temp").asDouble());
            dto.setFeelsLike(main.path("feels_like").asDouble());
            dto.setTempMin(main.path("temp_min").asDouble());
            dto.setTempMax(main.path("temp_max").asDouble());
            dto.setHumidity(main.path("humidity").asInt());
        }

        if (root.has("wind")) {
            dto.setWindSpeed(root.path("wind").path("speed").asDouble());
        }

        if (root.has("weather") && root.path("weather").isArray() && !root.path("weather").isEmpty()) {
            JsonNode weather = root.path("weather").get(0);
            dto.setCondition(weather.path("main").asText(""));
            dto.setDescription(weather.path("description").asText(""));
            dto.setIcon(weather.path("icon").asText(""));
        }

        return dto;
    }

    @Override
    public WeatherDto getDefaultWeatherData() {
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
