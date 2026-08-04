package com.market.finder.service.weather;

import com.market.finder.dto.WeatherDto;

public interface WeatherFallbackProvider {
    WeatherDto getFallbackWeather(String locationName, String errorMessage);
}
