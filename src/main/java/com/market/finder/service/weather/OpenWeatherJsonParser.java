package com.market.finder.service.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.finder.dto.WeatherDto;
import org.springframework.stereotype.Component;

@Component
public class OpenWeatherJsonParser implements WeatherParser {

    private final ObjectMapper objectMapper;

    public OpenWeatherJsonParser() {
        this.objectMapper = new ObjectMapper();
    }

    public OpenWeatherJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public WeatherDto parse(String rawJson, String defaultCityName) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);
            WeatherDto dto = new WeatherDto();
            dto.setCityName(root.path("name").asText(defaultCityName));

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
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse weather JSON payload", e);
        }
    }
}
