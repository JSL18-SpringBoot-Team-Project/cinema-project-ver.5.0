package com.movie.controller;

import com.movie.domain.Events;
import com.movie.domain.SessionUser;
import com.movie.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

	private final EventService eventService;

	@GetMapping("/")
	public String eventSingle(Model model) {
		List<Events> eventStart = eventService.eventStart();
		List<Events> eventEnd = eventService.eventEnd();
		model.addAttribute("start", eventStart);
		model.addAttribute("end", eventEnd);
		model.addAttribute("content", "events/event_single");
		model.addAttribute("title", "イベント");

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
		model.addAttribute("title", "イベント");

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
			response.put("message", "ログインしてください。");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		try {
			Integer userId = sessionUser.getId();

			// 이미 쿠폰을 받았는지 확인
			long existingCouponCount = eventService.userid(userId, couponId);
			if (existingCouponCount > 0) {
				response.put("status", "duplicate");
				response.put("message", "このクーポンはすでに受け取っています。");
				return ResponseEntity.ok(response);
			}

			// 쿠폰 생성
			LocalDateTime startDateTime = LocalDateTime.now();
			LocalDateTime endDateTime = startDateTime.plusDays(7); // 7일 후 계산
			eventService.usercoupon(userId, couponId, startDateTime, endDateTime);

			response.put("status", "success");
			response.put("message", "クーポンが正常に受け取りました。");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "クーポンの受け取り中にエラーが発生しました。");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
