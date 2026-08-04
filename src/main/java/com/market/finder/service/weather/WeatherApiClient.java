package com.market.finder.service.weather;

public interface WeatherApiClient {
    String fetchByCity(String city);
    String fetchByCoords(double lat, double lon);
}
