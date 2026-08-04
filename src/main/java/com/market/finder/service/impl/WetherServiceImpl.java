package com.market.finder.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.finder.dto.WeatherDto;
import com.market.finder.service.WetherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WetherServiceImpl implements WetherService {

    private static final Logger logger = LoggerFactory.getLogger(WetherServiceImpl.class);

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url:https://api.openweathermap.org/data/2.5/weather}")
    private String apiUrl;

    @Value("${weather.api.default-city:London}")
    private String defaultCity;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private volatile boolean enabled = false;

    public WetherServiceImpl() {
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    public WetherServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public WeatherDto getWeatherData(String city) {
        String targetCity = (city == null || city.trim().isEmpty()) ? defaultCity : city.trim();

        try {
            String url = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("q", targetCity)
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .toUriString();

            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null) {
                return WeatherDto.error("No response received from weather provider.");
            }

            JsonNode root = objectMapper.readTree(response);

            WeatherDto dto = new WeatherDto();
            dto.setCityName(root.path("name").asText(targetCity));

            return getWeatherDto(root, dto);
        } catch (Exception e) {
            logger.error("Error fetching weather data for city {}: {}", targetCity, e.getMessage());
            return WeatherDto.error("Could not fetch weather data for '" + targetCity + "': " + e.getMessage());
        }
    }

    @Override
    public WeatherDto getWeatherDataByCoords(double lat, double lon) {
        try {
            String url = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .toUriString();

            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null) {
                return WeatherDto.error("No response received from weather provider.");
            }

            JsonNode root = objectMapper.readTree(response);

            WeatherDto dto = new WeatherDto();
            dto.setCityName(root.path("name").asText("Current Location"));

            return getWeatherDto(root, dto);
        } catch (Exception e) {
            logger.error("Error fetching weather data for coords ({}, {}): {}", lat, lon, e.getMessage());
            return WeatherDto.error("Could not fetch weather data for coordinates (" + lat + ", " + lon + "): " + e.getMessage());
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
        return getWeatherData(defaultCity);
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
