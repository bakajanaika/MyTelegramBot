package com.example.mytgbot.services;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class ApodService {
    public String getCosmo() {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=U8yPG2g3pKQT26SSH0UknNTiSlJEAn2JUyoNPXmj";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        String responseBody = responseEntity.getBody();
        try {
            JSONObject jsonObject = new JSONObject(responseBody);

            return jsonObject.getString("hdurl");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing JSON response";
        }
    }

    public String getCosmoExplanation(){
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=U8yPG2g3pKQT26SSH0UknNTiSlJEAn2JUyoNPXmj";

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        String responseBody = responseEntity.getBody();
        return responseBody;
    }
}


