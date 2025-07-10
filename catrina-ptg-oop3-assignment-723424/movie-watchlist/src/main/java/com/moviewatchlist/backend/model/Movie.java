package com.moviewatchlist.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String year;
    private String director;
    private String genre;

    @Column(length = 1000)
    private String similarMovies;

    private String imagePath;
    private boolean watched;
    private int rating;
}
