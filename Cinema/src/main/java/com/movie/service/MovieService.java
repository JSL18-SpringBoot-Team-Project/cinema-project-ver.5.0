package com.movie.service;

import com.movie.domain.Movies;
import com.movie.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    @Autowired
    private final MovieMapper movieMapper;

    public long insertMovie(Movies movies) {
        return movieMapper.insertMovie(movies);
    }

    public List<Movies> movieManageList() {

        List<Movies> movies = movieMapper.movieManageList();

        for (Movies movie : movies) {
            if(movie.getTitle().length() > 15) {
                movie.setTitle(movie.getTitle().substring(0, 15) + "…");
            }
        }

        return movies;
    }

    public Movies movieInfo(long id) {
        return movieMapper.movieInfo(id);
    }

    public long updateMovie(Movies movies) {
        return movieMapper.updateMovie(movies);
    }

    public long deleteMovie(long id) {
        return movieMapper.deleteMovie(id);
    }

    public List<Movies> upcomingMovie() {

        return movieMapper.upcomingMovie();

    }

    public List<Movies> upcomingSoonMovies() {
        return movieMapper.upcomingSoonMovies();
    }

    public List<Movies> recentlyEndedMovies() {
        return movieMapper.recentlyEndedMovies();
    }

//    public List<Movies> releasedMovie() {
//
//        List<Movies> movies = movieMapper.releasedMovie();
//
//        for(Movies movie : movies) {
//
//            movie.setPosterImg(movie.getPosterImg().replace("\\", "/"));
//
//        }
//
//        return movies;
//    }
//
//    public List<Movies> bestMovies() {
//
//        List<Movies> movies = movieMapper.bestMovies();
//
//        for(Movies movie : movies) {
//
//            movie.setPosterImg(movie.getPosterImg().replace("\\", "/"));
//
//        }
//
//        return movies;
//    }

    public List<Movies> searchMoviesByTitle(String title) {
        return movieMapper.searchMoviesByTitle(title);
    }

    public void updateMovieAudience(long id) {
        movieMapper.updateMovieAudience(id);
    }
}
