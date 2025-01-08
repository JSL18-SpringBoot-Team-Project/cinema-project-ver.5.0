package com.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @GetMapping("/")
    public String myPage(Model model) {
        // 컨텐츠에 mypage 템플릿 경로 추가
        model.addAttribute("content", "mypage/mypage");
        model.addAttribute("title", "マイページ");
        return "layout/base";
    }
}
