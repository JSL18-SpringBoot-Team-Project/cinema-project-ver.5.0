package com.movie.service;

import com.movie.domain.MovieDTO;
import com.movie.domain.MovieDetails;
import com.movie.mapper.MovieDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
