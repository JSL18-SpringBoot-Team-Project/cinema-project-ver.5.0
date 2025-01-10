package com.movie.controller;

import com.movie.domain.Events;
import com.movie.domain.Inquiries;
import com.movie.domain.Movies;
import com.movie.domain.SessionUser;
import com.movie.service.EmailService;
import com.movie.service.EventService;
import com.movie.service.InquiryService;
import com.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

	@Autowired
	private InquiryService inquiryService;

	@Autowired
	private MovieService movieService;

	@Autowired
	private EventService eventService;

	@Autowired
	private EmailService emailService;

	@GetMapping({"", "/", "/index"})
	public String index(Model model) {
		List<Movies> upcoming = movieService.upcomingMovie();
		List<Movies> upcomingSoon = movieService.upcomingSoonMovies();
		List<Movies> recentlyEnded = movieService.recentlyEndedMovies();
		List<Events> mainEvents = eventService.eventStart();

		model.addAttribute("event", mainEvents);
		model.addAttribute("upcoming", upcoming);
		model.addAttribute("upcomingSoon", upcomingSoon);
		model.addAttribute("recentlyEnded", recentlyEnded);

		model.addAttribute("content", "main/index");
		model.addAttribute("title", "ホーム");
		return "layout/base";
	}

	@GetMapping("/contact")
	public String contact(Model model, @ModelAttribute("sessionUser") SessionUser sessionUser) {
		if (sessionUser != null) {
			model.addAttribute("name", sessionUser.getName());
			model.addAttribute("email", sessionUser.getEmail());
			model.addAttribute("content", "main/contact");
			model.addAttribute("title", "お問い合わせ");
		} else {
			return "redirect:/";
		}
		return "layout/base";
	}

	@PostMapping("/contact")
	public String sendContact(@ModelAttribute("sessionUser") SessionUser sessionUser, @ModelAttribute Inquiries inquiry, Model model) {
		System.out.println("sendContact 들어옴");
		if (sessionUser == null) {
			return "redirect:/";
		}
			model.addAttribute("email", sessionUser.getEmail());
		if(inquiry.getContent().trim().isEmpty() || inquiry.getInquiry_type() == null) {
			model.addAttribute("error", "全ての値を入力してください。");
		} else {
			try {
				inquiry.setUser_id(sessionUser.getId());
				inquiry.setEmail(sessionUser.getEmail());
				inquiryService.insertInquiry(inquiry);
				emailService.sendContactEmail(sessionUser.getEmail(), inquiry.getContent());
				model.addAttribute("success", "お問い合わせが正常に送信されました。");
			} catch (Exception e) {
			model.addAttribute("email", sessionUser.getEmail());
				model.addAttribute("error", "エラーが発生しました。");
			}
		}
			model.addAttribute("title", "お問い合わせ");
			model.addAttribute("content", "main/contact");
			return "layout/base";
	}
}
