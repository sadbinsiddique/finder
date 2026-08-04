package com.market.finder.service;

import com.market.finder.dto.WeatherDto;

public interface WetherService {
    WeatherDto getWeatherData(String city);
    WeatherDto getWeatherDataByCoords(double lat, double lon);
    WeatherDto getDefaultWeatherData();
    boolean isEnabled();
    void setEnabled(boolean enabled);
}

