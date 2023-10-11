package com.example.mytgbot.services;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class CatService {
    public String getCat() {


        String apiKey = "live_YIMm5WN1rUdfCM2LdjwREDyVXwAlegBpwbumuwpmd3vbmCWoVrcFpZcVKZD6xUk2";
        String apiUrl = "https://api.thecatapi.com/v1/images/search";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        String responseBody = responseEntity.getBody();
        try {
            JSONArray jsonArray = new JSONArray(responseBody);

            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                return jsonObject.getString("url");
            } else {
                return "No images found in JSON response";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error parsing JSON response";
        }
    }
}
