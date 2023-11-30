package com.example.mytgbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@RequiredArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class WeatherData {

    private Location location;
    private Current current;

}
