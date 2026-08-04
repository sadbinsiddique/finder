package com.market.finder.service.weather;

import com.market.finder.entity.WeatherDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultWeatherFallbackProvider implements WeatherFallbackProvider {

    @Value("${weather.api.default-city:Dhaka}")
    private String defaultCity;

    @Override
    public WeatherDto getFallbackWeather(String locationName, String errorMessage) {
        WeatherDto dto = WeatherDto.error(errorMessage);
        dto.setCityName(locationName != null && !locationName.trim().isEmpty() ? locationName : defaultCity);
        dto.setCountry("BD");
        dto.setTemperature(28.5);
        dto.setFeelsLike(31.0);
        dto.setTempMin(25.0);
        dto.setTempMax(33.0);
        dto.setHumidity(70);
        dto.setWindSpeed(3.6);
        dto.setCondition("Clear");
        dto.setDescription("API key pending activation - Showing fallback weather");
        dto.setIcon("01d");
        return dto;
    }
}
