package com.moviewatchlist.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TmdbClient {

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> fetchImagePaths(String title) {
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + tmdbApiKey + "&query=" + title;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || response.get("results") == null) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        if (results.isEmpty()) return new ArrayList<>();

        Map<String, Object> movie = results.get(0);
        Integer id = (Integer) movie.get("id");

        String imageUrl = "https://api.themoviedb.org/3/movie/" + id + "/images?api_key=" + tmdbApiKey;
        Map<String, Object> imageResponse = restTemplate.getForObject(imageUrl, Map.class);

        List<Map<String, Object>> backdrops = (List<Map<String, Object>>) imageResponse.get("backdrops");
        List<String> paths = new ArrayList<>();

        for (int i = 0; i < Math.min(3, backdrops.size()); i++) {
            paths.add("https://image.tmdb.org/t/p/w780" + backdrops.get(i).get("file_path"));
        }

        return paths;
    }

    public List<String> fetchSimilarMovies(String title) {
        String searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + tmdbApiKey + "&query=" + title;
        Map<String, Object> searchResponse = restTemplate.getForObject(searchUrl, Map.class);

        if (searchResponse == null || searchResponse.get("results") == null) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) searchResponse.get("results");
        if (results.isEmpty()) return new ArrayList<>();

        Integer id = (Integer) results.get(0).get("id");
        String similarUrl = "https://api.themoviedb.org/3/movie/" + id + "/similar?api_key=" + tmdbApiKey;

        Map<String, Object> similarResponse = restTemplate.getForObject(similarUrl, Map.class);
        List<Map<String, Object>> similarResults = (List<Map<String, Object>>) similarResponse.get("results");

        List<String> titles = new ArrayList<>();
        for (int i = 0; i < Math.min(3, similarResults.size()); i++) {
            titles.add((String) similarResults.get(i).get("title"));
        }

        return titles;
    }
}
