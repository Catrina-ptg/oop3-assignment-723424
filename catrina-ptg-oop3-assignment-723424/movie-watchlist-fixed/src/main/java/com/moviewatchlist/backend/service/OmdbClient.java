package com.moviewatchlist.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Service
public class OmdbClient {

    private final String apiKey = "YOUR_OMDB_API_KEY";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> fetchMovieData(String title) {
        String url = "http://www.omdbapi.com/?t=" + title + "&apikey=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);

        Map<String, String> data = new HashMap<>();
        data.put("title", json.optString("Title"));
        data.put("year", json.optString("Year"));
        data.put("director", json.optString("Director"));
        data.put("genre", json.optString("Genre"));

        return data;
    }
}
