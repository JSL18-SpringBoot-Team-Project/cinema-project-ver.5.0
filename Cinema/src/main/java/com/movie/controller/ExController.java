package com.movie.controller;

import com.movie.domain.MovieDTO;
import com.movie.domain.Movies;
import com.movie.domain.SessionUser;
import com.movie.domain.User;
import com.movie.service.MovieDetailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class ExController {

    @Autowired
    private MovieDetailService movieDetailService;

    @GetMapping("/oo")
    public String oo(Model model, @ModelAttribute("sessionUser")SessionUser sessionUser) {
        // 세션에서 사용자 정보 가져오기
        if (sessionUser != null) {
            model.addAttribute("id", sessionUser.getId());
            model.addAttribute("name", sessionUser.getEmail());
            model.addAttribute("role", sessionUser.getRole());
        } else {
            model.addAttribute("error", "로그인 상태 X");
        }

        model.addAttribute("title", "Test");
        model.addAttribute("content", "test/oo");

        return "layout/base";
    }

    @GetMapping("/gogo")
    public String gogo(Model model) {

        List<Movies> movie = movieDetailService.getMovie();
        model.addAttribute("movie", movie);
        model.addAttribute("title", "test222");
        model.addAttribute("content", "test/gogo");
        return "layout/base";
    }

}
