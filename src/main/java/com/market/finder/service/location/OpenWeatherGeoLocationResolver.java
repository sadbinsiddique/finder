package com.market.finder.service.location;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OpenWeatherGeoLocationResolver implements GeoLocationService {

    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherGeoLocationResolver.class);

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.geo-url:https://api.openweathermap.org/geo/1.0/reverse}")
    private String geoUrl;

    @Value("${weather.api.default-city:Dhaka}")
    private String defaultCity;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OpenWeatherGeoLocationResolver(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public String resolveLocationName(double lat, double lon) {
        try {
            String url = UriComponentsBuilder.fromUriString(geoUrl)
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("limit", 1)
                    .queryParam("appid", apiKey)
                    .toUriString();

            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response != null && !response.trim().isEmpty()) {
                JsonNode array = objectMapper.readTree(response);
                if (array.isArray() && !array.isEmpty()) {
                    JsonNode location = array.get(0);
                    if (location.has("name") && !location.path("name").asText().isEmpty()) {
                        return location.path("name").asText();
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Could not resolve location name for coords ({}, {}): {}", lat, lon, e.getMessage());
        }
        return defaultCity;
    }

    @Override
    public String detectCurrentLocationName() {
        try {
            String response = webClient.get()
                    .uri("http://ip-api.com/json/")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response != null && !response.trim().isEmpty()) {
                JsonNode root = objectMapper.readTree(response);
                if ("success".equalsIgnoreCase(root.path("status").asText()) 
                        && root.has("city") && !root.path("city").asText().isEmpty()) {
                    return root.path("city").asText();
                }
            }
        } catch (Exception e) {
            logger.debug("Could not detect current IP location: {}", e.getMessage());
        }
        return defaultCity;
    }
}
