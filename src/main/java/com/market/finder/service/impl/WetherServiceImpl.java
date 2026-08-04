package com.market.finder.service.impl;

import com.market.finder.dto.WeatherDto;
import com.market.finder.service.WetherService;
import com.market.finder.service.location.GeoLocationService;
import com.market.finder.service.weather.WeatherApiClient;
import com.market.finder.service.weather.WeatherFallbackProvider;
import com.market.finder.service.weather.WeatherParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class WetherServiceImpl implements WetherService {

    private static final Logger logger = LoggerFactory.getLogger(WetherServiceImpl.class);

    @Value("${weather.api.default-city:London}")
    private String defaultCity;

    private final WeatherApiClient apiClient;
    private final WeatherParser parser;
    private final WeatherFallbackProvider fallbackProvider;
    private final GeoLocationService geoLocationService;

    private volatile boolean enabled = false;

    public WetherServiceImpl(WeatherApiClient apiClient,
                             WeatherParser parser,
                             WeatherFallbackProvider fallbackProvider,
                             GeoLocationService geoLocationService) {
        this.apiClient = apiClient;
        this.parser = parser;
        this.fallbackProvider = fallbackProvider;
        this.geoLocationService = geoLocationService;
    }

    @Override
    @Cacheable(value = "weatherCity", key = "#city != null ? #city : 'default'", unless = "#result == null || !#result.isSuccess()")
    public WeatherDto getWeatherData(String city) {
        String targetCity = (city == null || city.trim().isEmpty()) ? defaultCity : city.trim();

        try {
            String rawJson = apiClient.fetchByCity(targetCity);
            if (rawJson == null) {
                return fallbackProvider.getFallbackWeather(targetCity, "No response received from weather provider.");
            }
            return parser.parse(rawJson, targetCity);
        } catch (WebClientResponseException.Unauthorized e) {
            logger.warn("OpenWeatherMap API Key is unauthorized or pending activation (401) for city {}", targetCity);
            return fallbackProvider.getFallbackWeather(targetCity, "Weather API key unauthorized or pending activation (401). Showing fallback weather.");
        } catch (WebClientResponseException.TooManyRequests e) {
            logger.warn("OpenWeatherMap rate limit exceeded (429) for city {}", targetCity);
            return fallbackProvider.getFallbackWeather(targetCity, "Weather API rate limit exceeded (429). Showing fallback weather.");
        } catch (Exception e) {
            logger.error("Error fetching weather data for city {}: {}", targetCity, e.getMessage());
            return fallbackProvider.getFallbackWeather(targetCity, "Could not fetch weather data for '" + targetCity + "': " + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "weatherCoords", key = "T(java.lang.String).format('%.2f_%.2f', #lat, #lon)", unless = "#result == null || !#result.isSuccess()")
    public WeatherDto getWeatherDataByCoords(double lat, double lon) {
        String locationLabel = geoLocationService.resolveLocationName(lat, lon);

        try {
            String rawJson = apiClient.fetchByCoords(lat, lon);
            if (rawJson == null) {
                return fallbackProvider.getFallbackWeather(locationLabel, "No response received from weather provider.");
            }
            return parser.parse(rawJson, locationLabel);
        } catch (WebClientResponseException.Unauthorized e) {
            logger.warn("OpenWeatherMap API Key is unauthorized or pending activation (401) for coords ({}, {})", lat, lon);
            return fallbackProvider.getFallbackWeather(locationLabel, "Weather API key unauthorized or pending activation (401). Showing fallback weather.");
        } catch (WebClientResponseException.TooManyRequests e) {
            logger.warn("OpenWeatherMap rate limit exceeded (429) for coords ({}, {})", lat, lon);
            return fallbackProvider.getFallbackWeather(locationLabel, "Weather API rate limit exceeded (429). Showing fallback weather.");
        } catch (Exception e) {
            logger.error("Error fetching weather data for coords ({}, {}): {}", lat, lon, e.getMessage());
            return fallbackProvider.getFallbackWeather(locationLabel, "Could not fetch weather data for coordinates (" + lat + ", " + lon + "): " + e.getMessage());
        }
    }

    @Override
    public WeatherDto getDefaultWeatherData() {
        String detectedCity = geoLocationService.detectCurrentLocationName();
        return getWeatherData(detectedCity);
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
