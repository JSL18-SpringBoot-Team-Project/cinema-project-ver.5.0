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
import java.util.List;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieDetailService movieDetailService;

    @GetMapping("detail")
    public String movie(Model model, @RequestParam("id") long id){

        MovieDTO movie = movieDetailService.getMovieDetail(id);

        model.addAttribute("movie", movie.getMovies());
        model.addAttribute("detail", movie.getMovieDetails());
        model.addAttribute("now", LocalDate.now());//현재시간
        model.addAttribute("title","映画情報");
        model.addAttribute("content","movies/movie_single");
        return "layout/base";
    }

    @GetMapping("list")
    public String movieView(Model model) {
        model.addAttribute("title", "映画リスト");
        model.addAttribute("content", "movies/movie_list");

        List<Movies> movielist = movieDetailService.getMovie(); //전체 리스트
        List<Movies> moviesStart = movieDetailService.startMovie(); //현재 상영중
        List<Movies> scheduleMovie = movieDetailService.scheduleMovie(); //상영예정
        List<Movies> movieEnd = movieDetailService.endMovie(); // 상영끝난

        model.addAttribute("movie", movielist);
        model.addAttribute("start",moviesStart);
        model.addAttribute("schedule",scheduleMovie);
        model.addAttribute("end",movieEnd);

        return "layout/base";
    }

}
