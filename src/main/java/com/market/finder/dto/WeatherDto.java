package com.market.finder.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WeatherDto {
    private String cityName;
    private String country;
    private double temperature;
    private double feelsLike;
    private double tempMin;
    private double tempMax;
    private int humidity;
    private double windSpeed;
    private String condition;
    private String description;
    private String icon;
    private boolean success = true;
    private String errorMessage;

    public String getIconUrl() {
        if (icon != null && !icon.isBlank()) {
            if (icon.startsWith("http")) {
                return icon;
            }
            return "https://openweathermap.org/img/wn/" + icon + "@2x.png";
        }
        return "https://openweathermap.org/img/wn/02d@2x.png";
    }

    public static WeatherDto error(String message) {
        WeatherDto dto = new WeatherDto();
        dto.setSuccess(false);
        dto.setErrorMessage(message);
        return dto;
    }
}
