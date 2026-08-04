package com.market.finder.service.weather;

import com.market.finder.entity.WeatherDto;

public interface WeatherParser {
    WeatherDto parse(String rawJson, String defaultCityName);
}
