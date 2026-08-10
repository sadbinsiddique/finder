package com.market.finder.service.weather;

import com.market.finder.dto.WeatherDto;

public interface WeatherService {
    WeatherDto getWeatherData(String city);
    WeatherDto getWeatherDataByCoords(double lat, double lon);
    WeatherDto getDefaultWeatherData();
    boolean isEnabled();
    void setEnabled(boolean enabled);
}
