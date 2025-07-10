package com.moviewatchlist.backend.service;

import com.moviewatchlist.backend.model.Movie;
import com.moviewatchlist.backend.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MovieService {

    @Autowired private OmdbClient omdbClient;
    @Autowired private TmdbClient tmdbClient;
    @Autowired private ImageDownloader imageDownloader;
    @Autowired private MovieRepository movieRepository;

    public Movie addMovie(String title) {
        CompletableFuture var1 = CompletableFuture.supplyAsync(() -> omdbClient.fetchMovieData(title));
        CompletableFuture var2 = CompletableFuture.supplyAsync(() -> tmdbClient.fetchImagePaths(title));
        CompletableFuture var3 = CompletableFuture.supplyAsync(() -> tmdbClient.fetchSimilarMovies(title));

        try {
            var1.join(); var2.join(); var3.join();
            var movieData = (java.util.Map<String, String>) var1.get();
            var imageUrls = (List<String>) var2.get();
            var similarTitles = (List<String>) var3.get();

            String folder = imageDownloader.downloadImages(title, imageUrls);

            Movie movie = Movie.builder()
                .title(movieData.get("title"))
                .year(movieData.get("year"))
                .director(movieData.get("director"))
                .genre(movieData.get("genre"))
                .similarMovies(String.join(", ", similarTitles))
                .imagePath(folder)
                .watched(false)
                .rating(0)
                .build();

            return movieRepository.save(movie);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch movie data", e);
        }
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie updateWatched(Long id, boolean watched) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        movie.setWatched(watched);
        return movieRepository.save(movie);
    }

    public Movie updateRating(Long id, int rating) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        movie.setRating(rating);
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
