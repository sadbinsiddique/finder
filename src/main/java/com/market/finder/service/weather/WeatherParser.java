package com.market.finder.service.weather;

import com.market.finder.dto.WeatherDto;

public interface WeatherParser {
    WeatherDto parse(String rawJson, String defaultCityName);
}
