package com.example.mytgbot.services;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


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
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            String response = jsonObject.getString("explanation");

            String sourceLanguage = "auto";
            String targetLanguage = "ru";

            String url = "https://translate.googleapis.com/translate_a/single" +
                    "?client=gtx" +
                    "&sl=" + sourceLanguage +
                    "&tl=" + targetLanguage +
                    "&dt=t&q=" + URLEncoder.encode(response, StandardCharsets.UTF_8);


            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder responsed = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    responsed.append(inputLine);
                }
                in.close();
                return responsed.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }
}


