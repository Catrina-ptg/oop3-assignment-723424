package com.moviewatchlist.backend.controller;

import com.moviewatchlist.backend.model.Movie;
import com.moviewatchlist.backend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired private MovieService movieService;

    @GetMapping
    public List<Movie> getAll() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public Movie add(@RequestParam String title) {
        return movieService.addMovie(title);
    }

    @PatchMapping("/{id}/watched")
    public Movie updateWatched(@PathVariable Long id, @RequestParam boolean watched) {
        return movieService.updateWatched(id, watched);
    }

    @PatchMapping("/{id}/rating")
    public Movie updateRating(@PathVariable Long id, @RequestParam int rating) {
        return movieService.updateRating(id, rating);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
}
