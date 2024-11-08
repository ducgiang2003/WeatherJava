package com.todo.weatherapi1.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {
    private String country;
    private String temperature;
    private String description;
    private String dateTime;
}
