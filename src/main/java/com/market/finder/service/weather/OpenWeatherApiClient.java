package com.market.finder.service.weather;

import com.market.finder.service.helper.HttpHelperService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OpenWeatherApiClient implements WeatherApiClient {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url:https://api.openweathermap.org/data/2.5/weather}")
    private String apiUrl;

    private final HttpHelperService httpHelperService;

    public OpenWeatherApiClient(HttpHelperService httpHelperService) {
        this.httpHelperService = httpHelperService;
    }

    @Override
    public String fetchByCity(String city) {
        String url = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        return httpHelperService.get(url);
    }

    @Override
    public String fetchByCoords(double lat, double lon) {
        String url = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        return httpHelperService.get(url);
    }
}

