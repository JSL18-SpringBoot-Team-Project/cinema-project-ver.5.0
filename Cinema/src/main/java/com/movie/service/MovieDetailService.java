package com.movie.service;

import com.movie.domain.MovieDTO;
import com.movie.domain.MovieDetails;
import com.movie.domain.Movies;
import com.movie.mapper.MovieDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieDetailService {

    private final MovieDetailMapper movieDetailMapper;

    public long insertMovieDetail(MovieDetails movieDetails) {
        return movieDetailMapper.insertMovieDetail(movieDetails);
    }

    public MovieDTO getMovieDetail(long movieId) {
        return movieDetailMapper.getMovieDetail(movieId);
    }

    public List<Movies> getMovie() {
        return movieDetailMapper.getMovie();
    }

    public List<Movies> endMovie() {
        return movieDetailMapper.endMovie();
    }

    public List<Movies> scheduleMovie() {
        return  movieDetailMapper.scheduleMovie();
    }

    public List<Movies> startMovie() {
        return  movieDetailMapper.startMovie();
    }
}
