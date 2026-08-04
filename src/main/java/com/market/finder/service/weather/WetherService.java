package com.market.finder.service.weather;

import com.market.finder.entity.WeatherDto;

public interface WetherService {
    WeatherDto getWeatherData(String city);
    WeatherDto getWeatherDataByCoords(double lat, double lon);
    WeatherDto getDefaultWeatherData();
    boolean isEnabled();
    void setEnabled(boolean enabled);
}
