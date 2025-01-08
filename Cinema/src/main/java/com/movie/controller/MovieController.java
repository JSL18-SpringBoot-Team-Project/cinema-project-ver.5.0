package com.movie.controller;

import com.movie.domain.MovieDTO;
import com.movie.domain.MovieDetails;
import com.movie.domain.Movies;
import com.movie.service.MovieDetailService;
import com.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieDetailService movieDetailService;

    @GetMapping("/detail")
    public String movie(Model model, @RequestParam("id") long id){
        System.out.println("id: " + id);


        MovieDTO movie = movieDetailService.getMovieDetail(id);
        System.out.println("content넘어옴: " + movie.getMovieDetails().getContent());
        System.out.println("anjfrkfy"+movie.getMovieDetails().getActor());


        model.addAttribute("movie", movie.getMovies());
        model.addAttribute("detail", movie.getMovieDetails());
        model.addAttribute("now", LocalDate.now());//현재시간
        model.addAttribute("title","test");
        model.addAttribute("content","movies/movie_single");

        return "layout/base";
    }

    @GetMapping("/list")
    public String movieView(Model model) {
        model.addAttribute("content", "movies/movie_list");

        return "layout/base";
    }

}
