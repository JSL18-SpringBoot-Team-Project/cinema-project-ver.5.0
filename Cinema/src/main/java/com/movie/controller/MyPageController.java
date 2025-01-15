package com.movie.controller;

import com.movie.domain.*;
import com.movie.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 마이페이지 메인 페이지
    @GetMapping("/")
    public String myPage(SessionUser sessionUser, Model model) {
        if (sessionUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // sessionUser에서 로그인한 사용자 ID 가져오기
        long userId = sessionUser.getId().longValue();
        String userName = sessionUser.getName(); // 사용자 이름

        // 사용자 정보
        model.addAttribute("userName", userName);

        // 사용자 티켓 및 쿠폰 개수 가져오기
        int ticketCount = myPageService.getTicketCount(userId);
        int couponCount = myPageService.getCouponCount(userId);
        model.addAttribute("ticketCount", ticketCount);
        model.addAttribute("couponCount", couponCount);

        // 사용자 예매 내역
        var bookingList = myPageService.getBookingList(userId, 1, 3);
        model.addAttribute("bookingList", bookingList);

        // 문의 내역 (샘플 데이터)
        var inquiryList = myPageService.(userId, "all", null);
        model.addAttribute("inquiryList", inquiryList);

        // 동적 콘텐츠 경로 추가
        model.addAttribute("content", "mypage/index");
        // 페이지 제목 추가
        model.addAttribute("title", "MY | マイページ");

        // 레이아웃으로 반환
        return "mypage/layout/base";
    }


    // 예매 내역 페이지
    @GetMapping("/booking/list")
    public String bookingList(SessionUser sessionUser,
                              @RequestParam(required = false) String title, // 검색 조건
                              @RequestParam(defaultValue = "1") int page,   // 기본 페이지 번호
                              @RequestParam(defaultValue = "1") int cancelPage, // 취소 내역 기본 페이지 번호
                              @RequestParam(defaultValue = "3") int pageSize, // 페이지당 항목 수
                              Model model) {
        if (sessionUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        long userId = sessionUser.getId();

        // 예매 내역 조회 (영화 제목 여부에 따라 다르게 처리)
        PagingDTO<Bookings> bookingList;
        if (title != null && !title.trim().isEmpty()) {
            bookingList = myPageService.searchBookingsByTitle(userId, title, lastBookingTimestamp, page, pageSize); // 검색된 예매 내역
        } else {
            bookingList = myPageService.getBookingList(userId, page, pageSize); // 전체 예매 내역
        }

        // 취소 내역 조회
        PagingDTO<Bookings> cancelList = myPageService.getCancelList(userId, cancelPage, pageSize);

        // 모델에 데이터 추가
        model.addAttribute("bookingList", bookingList);
        model.addAttribute("cancelList", cancelList);
        model.addAttribute("currentTitle", title); // 검색 조건 유지
        // 동적 콘텐츠 경로 추가
        model.addAttribute("content", "mypage/booking/booking_list");
        model.addAttribute("title", "MY | 예매내역");

        return "mypage/layout/base";
    }


    // 쿠폰 페이지
    @GetMapping("/coupon/list")
    public String coupon(SessionUser sessionUser,
                         @RequestParam(defaultValue = "all") String filter,
                         Model model) {

        if (sessionUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        long userId = sessionUser.getId().longValue();

        // 쿠폰 데이터 조회
        int couponCount = myPageService.getCouponCount(userId);
        List<UserCoupon> couponList = myPageService.getUserCouponList(userId, filter);

        model.addAttribute("couponCount", couponCount);
        model.addAttribute("couponList", couponList);
        model.addAttribute("filter", filter);
        // 동적 콘텐츠 경로 추가
        model.addAttribute("content", "mypage/coupon/coupon");
        model.addAttribute("title", "MY | 쿠폰");

        return "mypage/layout/base";
    }


    // 문의 내역 페이지
    @GetMapping("/inquiry/list")
    public String inquiryList(SessionUser sessionUser,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) String keyword,
                              Model model) {
        if (sessionUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        int userId = sessionUser.getId();

        // Service 호출
        List<Inquiries> inquiryList = myPageService.getInquiries(userId, status, keyword);

        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);
        // 동적 컨텐츠 경로
        model.addAttribute("content", "mypage/inquiry/inquiry_list");
        model.addAttribute("title", "MY | 문의내역");

        return "mypage/layout/base";
    }


    // 비밀번호 인증 페이지
    @GetMapping("/pw_verify")
    public String showPasswordVerifyPage(SessionUser sessionUser, Model model) {
        // 세션에 OAuth 사용자 정보 확인
        if (sessionUser.getSocialProvider() != null && sessionUser.getSocialProvider() != SocialProvider.NONE) {
            // OAuth 사용자는 비밀번호 인증 없이 바로 이동
            return "redirect:/mypage/profile";
        }

        // 동적 콘텐츠 경로 추가
        model.addAttribute("content", "mypage/user/pw_verify");
        model.addAttribute("title", "MY | 비밀번호 확인");
        return "mypage/layout/base";
    }

    // 비밀번호 인증 처리
    @PostMapping("/verifying")
    public String verifyPassword(@RequestParam("password") String password, SessionUser sessionUser, Model model) {
        long userId = sessionUser.getId();
        User user = userService.getUserInfo(sessionUser.getId());

        if (sessionUser.getSocialProvider() != null && sessionUser.getSocialProvider() != SocialProvider.NONE) {
            return "redirect:/mypage/profile"; // OAuth 사용자라면 비밀번호 확인 없이 회원정보 수정 페이지로 이동
        }

        if (!userService.verifyPassword(userId, password)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("content", "mypage/user/pw_verify");
            return "mypage/layout/base"; // 비밀번호 불일치 시 다시 입력 페이지로 이동
        }

        return "redirect:/mypage/profile"; // 인증 성공 -> 회원정보 수정 페이지로 이동
    }

    // 회원정보 수정 페이지
    @GetMapping("/profile")
    public String showProfilePage(SessionUser sessionUser, Model model) {
        long userId = sessionUser.getId();
        User user = userService.getUserInfo(sessionUser.getId());

        if (user == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }

        // 마지막 비밀번호 변경일 계산
        LocalDateTime lastPasswordChange = user.getLastPasswordChange();
        long sinceLastPasswordChange = lastPasswordChange != null ?
                ChronoUnit.DAYS.between(lastPasswordChange, LocalDateTime.now()) : 1;

        // 수정 가능 여부 설정
        boolean isEditable = sessionUser.getSocialProvider() == null || sessionUser.getSocialProvider() == SocialProvider.NONE;

        // 사용자 정보 모델에 추가
        model.addAttribute("user", user);
        model.addAttribute("isEditable", isEditable); // 수정 가능 여부
        model.addAttribute("sinceLastPasswordChange", sinceLastPasswordChange); // 마지막 비밀번호 변경일

        // 동적 콘텐츠 경로 추가
        model.addAttribute("content", "mypage/user/user_edit");
        model.addAttribute("title", "MY | 회원정보 수정");

        return "mypage/layout/base"; // 회원정보 수정 페이지 렌더링
    }

    // 회원정보 수정 처리
    @PostMapping("/update_user")
    public String updateUser(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("USER");
        // 세션 사용자와 요청 사용자 ID 검증
        if (sessionUser == null || !sessionUser.getId().equals(user.getId())) {
            throw new IllegalStateException("잘못된 요청입니다.");
        }

        // OAuth 사용자 수정 제한
        if (sessionUser.getSocialProvider() != null && sessionUser.getSocialProvider() != SocialProvider.NONE) {
            throw new IllegalStateException("OAuth 사용자 정보는 수정할 수 없습니다.");
        }
        
        // 유효성 검사
        validateUser(user);

        // 사용자 정보 업데이트
        try {
            userService.updateUserInfo(user);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원정보 수정 중 오류가 발생했습니다.");
            return "redirect:/mypage/profile";
        }

        // 세션 정보 갱신
        sessionUser.setName(user.getName());
        session.setAttribute("USER", sessionUser);

        // 성공 메시지 설정
        redirectAttributes.addFlashAttribute("successMessage", "회원정보가 성공적으로 수정되었습니다.");
        return "redirect:/mypage/profile";
    }

    // 유효성 검사 메서드
    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new IllegalArgumentException("유효한 이메일 주소를 입력해주세요.");
        }

        if (user.getPhone() != null && !user.getPhone().matches("^(010|011)-\\d{4}-\\d{4}$")) {
            throw new IllegalArgumentException("유효한 전화번호를 입력해주세요.");
        }
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 SessionUser sessionUser,
                                 RedirectAttributes redirectAttributes) {
        long userId = sessionUser.getId();

        if (sessionUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        try {
            // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            }

            // 비밀번호 변경 처리
            userService.changePassword(sessionUser.getId(), currentPassword, newPassword);

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            // 에러 메시지 설정
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/mypage/profile";
    }


}
