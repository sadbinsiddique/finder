package com.market.finder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDto {
    private String cityName;
    private String country;
    private Double temperature;
    private Double feelsLike;
    private Double tempMin;
    private Double tempMax;
    private Integer humidity;
    private Double windSpeed;
    private String condition;
    private String description;
    private String icon;
    private String iconUrl;
    private boolean success = true;
    private String errorMessage;

    public WeatherDto() {}

    public void setIcon(String icon) {
        this.icon = icon;
        if (icon != null && !icon.isEmpty()) {
            this.iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
        }
    }

    public static WeatherDto error(String message) {
        WeatherDto dto = new WeatherDto();
        dto.setSuccess(false);
        dto.setErrorMessage(message);
        return dto;
    }
}
