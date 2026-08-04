package com.market.finder.service.location;

public interface GeoLocationService {
    String resolveLocationName(double lat, double lon);
    String detectCurrentLocationName();
}
