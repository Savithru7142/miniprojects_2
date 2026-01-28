package com.klu.MovieWatchList.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
public class MovieWatchlistController {

    private final List<Movie> watchList = new ArrayList<>();
    private int idCounter = 400;

    // Constructor – sample data
    public MovieWatchlistController() {
        watchList.add(new Movie(401, "RRR", "Action", false, null, null));
        watchList.add(new Movie(402, "Inception", "Sci-Fi", true, 5, "Mind bending movie"));
        idCounter = 402;
    }

    // ================= GET ALL =================
    @GetMapping("/watchlist")
    public List<Movie> getAllMovies(
            @RequestParam(required = false) Boolean watched) {

        if (watched == null) {
            return watchList;
        }

        List<Movie> result = new ArrayList<>();
        for (Movie m : watchList) {
            if (m.watched == watched) {
                result.add(m);
            }
        }
        return result;
    }

    // ================= GET BY ID =================
    @GetMapping("/watchlist/{id}")
    public Movie getMovieById(@PathVariable int id) {
        return findById(id);
    }

    // ================= CREATE =================
    @PostMapping("/watchlist")
    public Movie addMovie(@RequestBody Movie newMovie) {
        newMovie.id = ++idCounter;
        newMovie.watched = false;
        newMovie.rating = null;
        newMovie.notes = null;
        watchList.add(newMovie);
        return newMovie;
    }

    // ================= UPDATE =================
    @PutMapping("/watchlist/{id}")
    public Movie updateMovie(@PathVariable int id,
                             @RequestBody Movie updatedMovie) {

        Movie existing = findById(id);
        if (existing == null) {
            return null;
        }

        existing.movieName = updatedMovie.movieName;
        existing.genre = updatedMovie.genre;
        existing.watched = updatedMovie.watched;
        existing.rating = updatedMovie.rating;
        existing.notes = updatedMovie.notes;

        return existing;
    }

    // ================= DELETE =================
    @DeleteMapping("/watchlist/{id}")
    public String deleteMovie(@PathVariable int id) {
        Movie m = findById(id);
        if (m == null) {
            return "Movie not found";
        }
        watchList.remove(m);
        return "Movie deleted successfully : " + id;
    }

    // ================= SEARCH =================
    @GetMapping("/watchlist/search")
    public List<Movie> searchMovies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean watched,
            @RequestParam(required = false) Integer minRating) {

        List<Movie> result = new ArrayList<>();

        for (Movie m : watchList) {
            if ((name == null || m.movieName.toLowerCase().contains(name.toLowerCase())) &&
                (genre == null || m.genre.equalsIgnoreCase(genre)) &&
                (watched == null || m.watched == watched) &&
                (minRating == null || (m.rating != null && m.rating >= minRating))) {

                result.add(m);
            }
        }
        return result;
    }

    // ================= FIND BY ID =================
    private Movie findById(int id) {
        for (Movie m : watchList) {
            if (m.id == id)
                return m;
        }
        return null;
    }
}

/* ================= POJO CLASS ================= */

class Movie {
    public int id;
    public String movieName;
    public String genre;
    public boolean watched;
    public Integer rating; // 1–5
    public String notes;

    public Movie() {}

    public Movie(int id, String movieName, String genre,
                 boolean watched, Integer rating, String notes) {
        this.id = id;
        this.movieName = movieName;
        this.genre = genre;
        this.watched = watched;
        this.rating = rating;
        this.notes = notes;
    }
}
