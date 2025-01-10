package com.movie.controller;//package com.movie.controller;
//
//import com.movie.domain.*;
//import com.movie.service.BookingService;
//import com.movie.service.CouponService;
//import com.movie.service.InquiryService;
//import com.movie.service.UserService;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/mypage")
//public class MyPageController {
//
//    @Autowired
//    private BookingService bookingService;
//
//    @Autowired
//    private InquiryService inquiryService;
//
//    @Autowired
//    private CouponService couponService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    /**
//     * 마이페이지(index) 화면을 렌더링하는 컨트롤러 메서드.
//     * 1. 사용자의 세션 정보를 기반으로 마이페이지 데이터를 조회.
//     * 2. 사용자 이름, 티켓/쿠폰 개수, 예매 내역, 문의 내역 등 다양한 정보를 Model에 추가.
//     *
//     */
//    @GetMapping("/")
//    public String myPage(SessionUser sessionUser, Model model) {
//        // sessionUser에서 로그인한 사용자 ID 가져오기
//        long userId = sessionUser.getId().longValue();
//        String userName = sessionUser.getName(); // 사용자 이름
//
//        // 사용자 정보
//        model.addAttribute("userName", userName);
//
//        // 사용자 티켓 및 쿠폰 개수 가져오기
//        int ticketCount = userService.getTicketCount(userId);
//        int couponCount = userService.getCouponCount(userId);
//        model.addAttribute("ticketCount", ticketCount);
//        model.addAttribute("couponCount", couponCount);
//
//        // 사용자 예매 내역
//        var bookingList = bookingService.getBookingsByUserId(userId); // `userId`를 기준으로 BookingService에서 예매 내역을 가져옴
//        model.addAttribute("bookingList", bookingList);
//
//        // 문의 내역 (샘플 데이터)
//        var inquiryList = inquiryService.getInquiriesByUserId(userId);
//        model.addAttribute("inquiryList", inquiryList);
//
//        // 동적 콘텐츠 경로 추가
//        model.addAttribute("content", "mypage/index");
//        // 페이지 제목 추가
//        model.addAttribute("title", "マイページ");
//
//        // 레이아웃으로 반환
//        return "mypage/layout/base";
//    }
//
//    /**
//     * 예매내역
//     *
//     */
//    @GetMapping("/booking/list")
//    public String bookingList(
//            @RequestParam(value = "type", required = false) String type,
//            @RequestParam(value = "startDate", required = false) String startDate,
//            @RequestParam(value = "endDate", required = false) String endDate,
//            SessionUser sessionUser, Model model
//    ) {
//        long userId = sessionUser.getId();
//
//        // 예매 내역
//        var bookingList = bookingService.getBookingsByUserId(userId, type, startDate, endDate);
//        model.addAttribute("bookingList", bookingList);
//
//        // 취소 내역
//        var cancellationList = bookingService.getCancellationList(userId);
//        model.addAttribute("cancellationList", cancellationList);
//
//        // 필터 값 모델에 추가
//        model.addAttribute("selectedType", type);
//        model.addAttribute("selectedStartDate", startDate);
//        model.addAttribute("selectedEndDate", endDate);
//
//        // 동적 콘텐츠 경로 추가
//        model.addAttribute("content", "mypage/booking/booking_list");
//        model.addAttribute("title", "예매내역");
//
//        return "mypage/layout/base";
//    }
//
//
//    /**
//     * Coupon
//     */
//    @GetMapping("/coupon")
//    public String coupon(SessionUser sessionUser, Model model) {
//        // sessionUser가 자동으로 주입됨
//        long userId = sessionUser.getId().longValue();
//
//        // 쿠폰 리스트
//        var couponList = couponService.getCouponListByUserId(userId);
//        model.addAttribute("couponList", couponList);
//
//        // 동적 콘텐츠 경로 추가
//        model.addAttribute("content", "mypage/coupon/coupon");
//        // 페이지 제목 추가
//        model.addAttribute("title", "쿠폰");
//
//        return "mypage/layout/base";
//    }
//
//    @PostMapping("/registerCoupon")
//    public String registerCoupon(@RequestParam("couponCode") long couponCode, SessionUser sessionUser) {
//        long userId = sessionUser.getId().longValue();
//
//        if (!couponService.isCouponCodeValid(couponCode)) {
//            throw new IllegalArgumentException("Invalid Coupon Code.");
//        }
//
//        couponService.registerCoupon(userId, couponCode);
//        return "redirect:/mypage/coupon";
//    }
//
//    @Autowired
//    public MyPageController(InquiryService inquiryService) {
//        this.inquiryService = inquiryService;
//    }
//
//    /**
//     * 문의 목록 페이지 조회 (필터 및 검색 포함)
//     */
//    @GetMapping("/mypage/inquiry/inquiry_list")
//    public String inquiryList(@RequestParam(value = "status", defaultValue = "all") String status,
//                              @RequestParam(value = "keyword", required = false) String keyword,
//                              @RequestParam(value = "page", defaultValue = "1") int page,
//                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
//                              Model model) {
//
//        // 서비스 호출하여 문의 목록 및 페이징 처리
//        PagingDTO<Inquiries> inquiries = inquiryService.getPagedInquiries(status, page, pageSize);
//
//        // 모델에 데이터 추가
//        model.addAttribute("inquiryList", inquiries.getContent());
//        model.addAttribute("paging", inquiries);
//        model.addAttribute("status", status);
//        model.addAttribute("keyword", keyword);
//
//        return "mypage/inquiry/inquiry_list";
//    }
//
//    /**
//     * 문의 등록 페이지 이동
//     */
//    @GetMapping("/inquiry_form")
//    public String inquiryForm(Model model) {
//        model.addAttribute("pageTitle", "문의하기");
//        return "mypage/inquiry/inquiry_form";
//    }
//
//    /**
//     * 문의 등록 처리
//     */
//    @PostMapping("/submit")
//    public String submitInquiry(@ModelAttribute Inquiries inquiries) {
//        inquiryService.insertInquiry(inquiries);
//        return "redirect:/mypage/inquiry/list";
//    }
//
//    /**
//     * 특정 사용자 문의 내역 조회
//     */
//    @GetMapping("/user/{userId}")
//    public String inquiriesByUserId(
//            @PathVariable("userId") long userId,
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
//            Model model) {
//
//        List<Inquiries> inquiries = inquiryService.getInquiriesByUserId(userId);
//        model.addAttribute("inquiries", inquiries);
//        model.addAttribute("userId", userId);
//
//        return "mypage/inquiry/user_inquiry_list"; // 사용자 문의 내역 화면
//    }
//
//    /**
//     * 에러 처리 예제
//     */
//    @ExceptionHandler(Exception.class)
//    public String handleError(Exception ex, Model model) {
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error/500error"; // 에러 페이지로 이동
//    }
//
//
//    // 비밀번호 인증 페이지
//    @GetMapping("/pw_verify")
//    public String showPasswordVerifyPage(SessionUser sessionUser, Model model) {
//        // 세션에 OAuth 사용자 정보 확인
//        if (sessionUser.getSocialProvider() != null && sessionUser.getSocialProvider() != SocialProvider.NONE) {
//            // OAuth 사용자는 비밀번호 인증 없이 바로 이동
//            return "redirect:/mypage/profile";
//        }
//        return "mypage/user/pw_verify";
//    }
//
//    // 비밀번호 인증 처리
//    @PostMapping("/pw_verify")
//    public String verifyPassword(@RequestParam("password") String password, SessionUser sessionUser, Model model) {
//        // 세션에서 사용자 ID 가져오기
//        long userId = sessionUser.getId();
//
//        // DB에서 사용자 정보 가져오기
//        User user = userService.getUserInfo(sessionUser.getId());
//
//        // OAuth 사용자라면 비밀번호 확인 없이 회원정보 수정 페이지로 이동
//        if (sessionUser.getSocialProvider() != null && sessionUser.getSocialProvider() != SocialProvider.NONE) {
//            return "redirect:/mypage/profile";
//        }
//
//        // 비밀번호 검증
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
//            return "mypage/user/pw_verify"; // 인증 실패 시 다시 비밀번호 입력 페이지로 이동
//        }
//
//        // 인증 성공 -> 회원정보 수정 페이지로 이동
//        return "redirect:/mypage/profile";
//    }
//
//    // 회원정보 수정 페이지
//    @GetMapping("/profile")
//    public String showProfilePage(SessionUser sessionUser, Model model) {
//        // DB에서 사용자 정보 가져오기
//        User user = userService.getUserInfo(sessionUser.getId());
//
//        // 사용자 정보 모델에 추가
//        model.addAttribute("user", user);
//        return "mypage/user/user_edit"; // 경로 수정
//    }
//
//    @PostMapping("/update_user")
//    public String updateUser(@ModelAttribute User user, HttpSession session, Model model) {
//        // 세션에서 사용자 확인
//        SessionUser sessionUser = (SessionUser) session.getAttribute("USER");
//        if (sessionUser == null || !sessionUser.getId().equals(user.getId())) {
//            throw new IllegalStateException("잘못된 요청입니다.");
//        }
//
//        // 사용자 정보 업데이트
//        userService.updateUserInfo(user);
//
//        // 업데이트 후 세션 정보 갱신
//        sessionUser.setName(user.getName());
//        session.setAttribute("USER", sessionUser);
//
//        // 성공 메시지 전달 후 회원정보 페이지로 리다이렉트
//        model.addAttribute("successMessage", "회원정보가 성공적으로 수정되었습니다.");
//        return "redirect:/mypage/profile";
//    }
//
//}
