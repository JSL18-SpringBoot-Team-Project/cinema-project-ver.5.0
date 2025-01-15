package com.movie.controller;

import com.movie.domain.Movies;
import com.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieSearchController {

    private final MovieService movieService;

    @GetMapping("api/movies/search")
    @ResponseBody
    public List<Movies> searchMovies(@RequestParam("title") String title) {

        return movieService.searchMoviesByTitle(title);
    }
}
