package com.market.finder.service.weather.impl;

import com.market.finder.service.location.GeoLocationService;
import com.market.finder.service.weather.WeatherApiClient;
import com.market.finder.service.weather.WeatherFallbackProvider;
import com.market.finder.service.weather.WeatherParser;
import com.market.finder.service.weather.WetherService;
import org.springframework.stereotype.Service;

/**
 * @deprecated Use {@link WeatherServiceImpl} instead.
 */
@Service
@Deprecated
public class WetherServiceImpl extends WeatherServiceImpl implements WetherService {

    public WetherServiceImpl(WeatherApiClient apiClient,
                             WeatherParser parser,
                             WeatherFallbackProvider fallbackProvider,
                             GeoLocationService geoLocationService) {
        super(apiClient, parser, fallbackProvider, geoLocationService);
    }
}

