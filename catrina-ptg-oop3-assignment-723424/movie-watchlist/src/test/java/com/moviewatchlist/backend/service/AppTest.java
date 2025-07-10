package com.moviewatchlist.backend.service;

import com.moviewatchlist.backend.model.Movie;
import com.moviewatchlist.backend.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private OmdbClient omdbMock;
    private TmdbClient tmdbMock;
    private ImageDownloader downloaderMock;
    private MovieRepository repoMock;
    private MovieService movieService;

    @BeforeEach
    void setup() {
        omdbMock = mock(OmdbClient.class);
        tmdbMock = mock(TmdbClient.class);
        downloaderMock = mock(ImageDownloader.class);
        repoMock = mock(MovieRepository.class);

        movieService = new MovieService(omdbMock, tmdbMock, downloaderMock, repoMock);
    }

    @Test
    void shouldAddMarvelThunderboltsMovieCorrectly() throws Exception {
        String title = "Thunderbolts*";

        when(omdbMock.fetchMovieData(title)).thenReturn(Map.of(
                "title", title,
                "year", "2025",
                "director", "Jake Schreier",
                "genre", "Action"
        ));

        when(tmdbMock.fetchImagePaths(title)).thenReturn(List.of("img1.jpg", "img2.jpg", "img3.jpg"));
        when(tmdbMock.fetchSimilarMovies(title)).thenReturn(List.of("Black Widow", "Captain America"));
        when(downloaderMock.downloadImages(eq(title), anyList())).thenReturn("/images/thunderbolts");

        Movie mockSaved = Movie.builder()
                .title(title)
                .year("2025")
                .director("Jake Schreier")
                .genre("Action")
                .similarMovies("Black Widow, Captain America")
                .imagePath("/images/thunderbolts")
                .watched(false)
                .rating(0)
                .build();

        when(repoMock.save(any(Movie.class))).thenReturn(mockSaved);

        Movie result = movieService.addMovie(title);

        JSONObject json = new JSONObject();
        json.put("title", result.getTitle());
        json.put("year", result.getYear());
        json.put("director", result.getDirector());
        json.put("genre", result.getGenre());
        json.put("watched", result.isWatched());
        json.put("rating", result.getRating());
        json.put("imagePath", result.getImagePath());
        json.put("similarMovies", result.getSimilarMovies());

        System.out.println("Added movie (Thunderbolts*): " + json.toString(2));

        assertEquals(title, result.getTitle());
        assertEquals("Action", result.getGenre());
        assertTrue(result.getSimilarMovies().contains("Black Widow"));
    }

    @Test
    void shouldAddClassicHalloweenMovieCorrectly() throws Exception {
        String title = "Halloween";

        when(omdbMock.fetchMovieData(title)).thenReturn(Map.of(
                "title", title,
                "year", "1978",
                "director", "John Carpenter",
                "genre", "Horror"
        ));

        when(tmdbMock.fetchImagePaths(title)).thenReturn(List.of("h1.jpg", "h2.jpg", "h3.jpg"));
        when(tmdbMock.fetchSimilarMovies(title)).thenReturn(List.of("Scream", "Friday the 13th"));
        when(downloaderMock.downloadImages(eq(title), anyList())).thenReturn("/images/halloween");

        Movie saved = Movie.builder()
                .title(title)
                .year("1978")
                .director("John Carpenter")
                .genre("Horror")
                .similarMovies("Scream, Friday the 13th")
                .imagePath("/images/halloween")
                .watched(false)
                .rating(0)
                .build();

        when(repoMock.save(any(Movie.class))).thenReturn(saved);

        Movie result = movieService.addMovie(title);

        JSONObject json = new JSONObject();
        json.put("title", result.getTitle());
        json.put("year", result.getYear());
        json.put("director", result.getDirector());
        json.put("genre", result.getGenre());
        json.put("watched", result.isWatched());
        json.put("rating", result.getRating());
        json.put("imagePath", result.getImagePath());
        json.put("similarMovies", result.getSimilarMovies());

        System.out.println("Added movie (Halloween): " + json.toString(2));

        assertEquals("Halloween", result.getTitle());
        assertEquals("John Carpenter", result.getDirector());
        assertTrue(result.getSimilarMovies().contains("Scream"));
    }
}
