package com.movie.controller;

import com.movie.domain.Coupons;
import com.movie.domain.Events;
import com.movie.domain.SessionUser;
import com.movie.domain.User;
import com.movie.service.EventService;
import com.movie.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

	@Autowired
	private EventService eventService;
	@Autowired
	private UserService userService;


	@GetMapping("/single")
	public String eventSingle(Model model) {
		List<Events> eventStart = eventService.eventStart();
		List<Events> eventEnd = eventService.eventEnd();
		model.addAttribute("start", eventStart);
		model.addAttribute("end", eventEnd);
		model.addAttribute("content", "events/event_single");
		model.addAttribute("title", "smkun");

		return "layout/base";
	}

	@GetMapping("/view")
	public String eventCategory(Model model, @RequestParam("id") long id) {
		// 이벤트 세부 정보를 가져옴
		Events eventview = eventService.eventDetail(id);
		LocalDate now = LocalDate.now();


		// 모델에 데이터를 추가
		model.addAttribute("view", eventview); // 올바르게 변수 사용
		model.addAttribute("now", now); // 현재 시간 전달
		model.addAttribute("content", "events/event_view");
		model.addAttribute("title", "smkun2");

		// 레이아웃으로 반환
		return "layout/base";
	}

	@PostMapping("/assign")
	public ResponseEntity<Map<String, Object>> assignCoupon(
			@RequestParam("couponId") long couponId,
			SessionUser sessionUser) {

		Map<String, Object> response = new HashMap<>();

		if (sessionUser == null) {
			response.put("status", "unauthorized");
			response.put("message", "로그인 하십시오.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		try {
			Integer userId = sessionUser.getId();
			System.out.println("로그인한 사용자 ID: " + userId);
			System.out.println("쿠폰 아이디: " + couponId);

			// 이미 쿠폰을 받았는지 확인
			long existingCouponCount = eventService.userid(userId, couponId);
			if (existingCouponCount > 0) {
				System.out.println("중복된 쿠폰: 사용자 ID " + userId + ", 쿠폰 ID " + couponId);
				response.put("status", "duplicate");
				response.put("message", "이미 쿠폰을 수령하셨습니다.");
				return ResponseEntity.ok(response);
			}

			// 쿠폰 생성
			LocalDateTime startDateTime = LocalDateTime.now();
			LocalDateTime endDateTime = startDateTime.plusDays(7); // 7일 후 계산
			eventService.usercoupon(userId, couponId, startDateTime, endDateTime);

			response.put("status", "success");
			response.put("message", "쿠폰이 성공적으로 수령되었습니다.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "쿠폰 수령 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
