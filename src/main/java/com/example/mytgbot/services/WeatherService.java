package com.example.mytgbot.services;
import com.example.mytgbot.models.WeatherData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {


    private final RestTemplate restTemplate;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public WeatherData fetchWeatherData(String apiUrl) {
        return restTemplate.getForObject(apiUrl, WeatherData.class);
    }
}