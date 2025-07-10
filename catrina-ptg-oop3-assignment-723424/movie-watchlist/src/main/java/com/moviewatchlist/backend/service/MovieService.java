package com.moviewatchlist.backend.service;

import com.moviewatchlist.backend.model.Movie;
import com.moviewatchlist.backend.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MovieService {

    private final OmdbClient omdbClient;
    private final TmdbClient tmdbClient;
    private final ImageDownloader imageDownloader;
    private final MovieRepository movieRepository;

    public MovieService(OmdbClient omdbClient,
                        TmdbClient tmdbClient,
                        ImageDownloader imageDownloader,
                        MovieRepository movieRepository) {
        this.omdbClient = omdbClient;
        this.tmdbClient = tmdbClient;
        this.imageDownloader = imageDownloader;
        this.movieRepository = movieRepository;
    }

    public Movie addMovie(String title) {
        CompletableFuture<?> omdbFuture = CompletableFuture.supplyAsync(() -> omdbClient.fetchMovieData(title));
        CompletableFuture<?> imageFuture = CompletableFuture.supplyAsync(() -> tmdbClient.fetchImagePaths(title));
        CompletableFuture<?> similarFuture = CompletableFuture.supplyAsync(() -> tmdbClient.fetchSimilarMovies(title));

        try {
            var omdbData = (java.util.Map<String, String>) omdbFuture.get();
            var imageUrls = (List<String>) imageFuture.get();
            var similarTitles = (List<String>) similarFuture.get();

            String folder = imageDownloader.downloadImages(title, imageUrls);

            Movie movie = Movie.builder()
                    .title(omdbData.get("title"))
                    .year(omdbData.get("year"))
                    .director(omdbData.get("director"))
                    .genre(omdbData.get("genre"))
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
