package com.moviewatchlist.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Service
public class TmdbClient {

    private final String apiKey = "YOUR_TMDB_API_KEY";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> fetchImagePaths(String title) {
        String searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + title;
        String response = restTemplate.getForObject(searchUrl, String.class);
        JSONObject json = new JSONObject(response);
        JSONArray results = json.getJSONArray("results");

        if (results.length() == 0) return List.of();

        int movieId = results.getJSONObject(0).getInt("id");
        String imagesUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/images?api_key=" + apiKey;
        String imagesResponse = restTemplate.getForObject(imagesUrl, String.class);
        JSONArray backdrops = new JSONObject(imagesResponse).getJSONArray("backdrops");

        List<String> paths = new ArrayList<>();
        for (int i = 0; i < Math.min(3, backdrops.length()); i++) {
            paths.add("https://image.tmdb.org/t/p/w780" + backdrops.getJSONObject(i).getString("file_path"));
        }

        return paths;
    }

    public List<String> fetchSimilarMovies(String title) {
        String searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + title;
        String response = restTemplate.getForObject(searchUrl, String.class);
        JSONObject json = new JSONObject(response);
        JSONArray results = json.getJSONArray("results");

        if (results.length() == 0) return List.of();

        int movieId = results.getJSONObject(0).getInt("id");
        String similarUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + apiKey;
        String similarResponse = restTemplate.getForObject(similarUrl, String.class);
        JSONArray similar = new JSONObject(similarResponse).getJSONArray("results");

        List<String> similarTitles = new ArrayList<>();
        for (int i = 0; i < Math.min(5, similar.length()); i++) {
            similarTitles.add(similar.getJSONObject(i).getString("title"));
        }

        return similarTitles;
    }
}
