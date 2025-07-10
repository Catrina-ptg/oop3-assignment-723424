package com.moviewatchlist.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class OmdbClient {

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> fetchMovieData(String title) {
        String url = "http://www.omdbapi.com/?t=" + title + "&apikey=" + omdbApiKey;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || response.get("Response").equals("False")) {
            throw new RuntimeException("Movie not found in OMDb: " + title);
        }

        Map<String, String> movieData = new HashMap<>();
        movieData.put("title", (String) response.get("Title"));
        movieData.put("year", (String) response.get("Year"));
        movieData.put("director", (String) response.get("Director"));
        movieData.put("genre", (String) response.get("Genre"));

        return movieData;
    }
}
