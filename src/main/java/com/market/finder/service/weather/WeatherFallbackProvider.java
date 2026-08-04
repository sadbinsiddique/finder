package com.market.finder.service.weather;

import com.market.finder.entity.WeatherDto;

public interface WeatherFallbackProvider {
    WeatherDto getFallbackWeather(String locationName, String errorMessage);
}
