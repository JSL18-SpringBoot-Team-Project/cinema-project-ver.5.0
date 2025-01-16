package com.movie.controller;

import com.movie.domain.*;
import com.movie.mapper.MyPageMapper;
import com.movie.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            throw new IllegalStateException("ログインが必要です。");
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

        // 예매 내역
        List<Bookings> bookingList = myPageService.getIndexBookingList(userId);
        model.addAttribute("bookingList", bookingList);

        // 문의 내역
        List<Inquiries> inquiriesList = myPageService.indexInquiry(sessionUser.getId());
        model.addAttribute("inquiryList", inquiriesList);

        // 동적 콘텐츠 경로 추가
        model.addAttribute("content", "mypage/index");
        model.addAttribute("title", "MY | マイページ");

        // 레이아웃으로 반환
        return "mypage/layout/base";
    }


    // 예매 내역 페이지
    @GetMapping("booking/list")
    public String bookingList(SessionUser sessionUser,
                              @RequestParam(value = "title", required = false) String title,
                              Model model) {
        if (sessionUser == null) {
            throw new IllegalStateException("ログインが必要です。");
        }

        long userId = sessionUser.getId();

        // 예매 내역 조회 (영화 제목 여부에 따라 다르게 처리)
        List<Bookings> bookingList = myPageService.getBookingListByTitle(userId, title);
        model.addAttribute("bookingList", bookingList);
        model.addAttribute("title", title); // 검색 조건 유지

//        // 취소 내역 조회
//        PagingDTO<Bookings> cancelList = myPageService.getCancelList(userId, cancelPage, pageSize);
//        model.addAttribute("cancelList", cancelList);

        model.addAttribute("content", "mypage/booking/booking_list");
        model.addAttribute("title", "MY | 予約履歴");

        return "mypage/layout/base";
    }


    // 쿠폰 페이지
    @GetMapping("/coupon/list")
    public String coupon(SessionUser sessionUser,
                         Model model) {

        if (sessionUser == null) {
            throw new IllegalStateException("ログインが必要です。");
        }

        long userId = sessionUser.getId().longValue();

        // 쿠폰 데이터 조회
        int couponCount = myPageService.getCouponCount(userId);
        model.addAttribute("couponCount", couponCount);

//        List<UserCoupon> couponList = myPageService.getUserCouponList(userId, filter);

//        model.addAttribute("couponList", couponList);
//        model.addAttribute("filter", filter);
//        // 동적 콘텐츠 경로 추가
        model.addAttribute("content", "mypage/coupon/coupon_list");
        model.addAttribute("title", "MY | 쿠폰");

        return "mypage/layout/base";
    }


    // 문의 내역 페이지
    @GetMapping("/inquiry/list")
    public String inquiryList(SessionUser sessionUser,
                              @RequestParam(value = "status", required = false, defaultValue = "all") String status,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              Model model) {
        if (sessionUser == null) {
            throw new IllegalStateException("ログインが必要です。");
        }

        Integer userId = sessionUser.getId();

        // 문의 내역
        List<Inquiries> inquiriesList = myPageService.indexInquiry(sessionUser.getId());
        model.addAttribute("inquiryList", inquiriesList);

        // 동적 컨텐츠 경로
        model.addAttribute("content", "mypage/inquiry/inquiry_list");
        model.addAttribute("title", "MY | 問い合わせ");

        return "mypage/layout/base";
    }

    // 문의 삭제
    @PostMapping("/inquiry/delete/{id}")
    public String deleteInquiry(
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        myPageService.deleteInquiry(id);
        redirectAttributes.addAttribute("successMessage", "문의가 성공적으로 삭제되었습니다.");
        return "redirect:/mypage/inquiry/list";
    }



    // 비밀번호 인증 페이지
    @GetMapping("pw_verify")
    public String showPasswordVerifyPage(SessionUser sessionUser, Model model) {
        if (sessionUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

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
    public String verifyPassword(@RequestParam("password") String password,
                                 @SessionAttribute(name = "sessionUser", required = false) SessionUser sessionUser,
                                 Model model) {
        if (sessionUser == null) {
            throw new IllegalStateException("ログインが必要です。");
        }

        long userId = sessionUser.getId();

        // 비밀번호 확인
        boolean isPasswordValid = myPageService.verifyPassword(userId, password);
        if (!isPasswordValid) {
            // 에러 메시지 및 다시 입력 페이지 설정
            model.addAttribute("error", "パスワードが一致しません。");
            model.addAttribute("content", "mypage/user/pw_verify");
            model.addAttribute("title", "비밀번호 확인");
            return "mypage/layout/base"; // 비밀번호 불일치 시 다시 렌더링
        }

        return "redirect:/mypage/profile"; // 인증 성공 -> 회원정보 수정 페이지로 이동
    }

    // 회원정보 수정 페이지
    @GetMapping("profile")
    public String showProfilePage(SessionUser sessionUser, Model model) {
        if (sessionUser == null) {
            throw new IllegalStateException("ログインが必要です。");
        }

        long userId = sessionUser.getId();
        User user = myPageService.getUserById(sessionUser.getId());

        if (user == null) {
            throw new IllegalArgumentException("ユーザー情報が見つかりません。");
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

    // 회원 탈퇴 처리
    @DeleteMapping("profile/{userId}/delete")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable long userId, SessionUser sessionUser, HttpSession session) {
        if (sessionUser == null || sessionUser.getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        try {
            userService.deleteUser(userId);

            // 세션 무효화 처리
            session.invalidate();

            return ResponseEntity.ok("탈퇴가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("탈퇴 처리 중 오류가 발생했습니다.");
        }
    }

    // 회원정보 수정 처리
    @PostMapping("update_user")
    public String updateUser(User user, @SessionAttribute(name = "sessionUser") SessionUser sessionUser, Model model) {
        if (sessionUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        user.setId(sessionUser.getId()); // 세션의 사용자 ID로 설정
        try {
            myPageService.updateUser(user);
            model.addAttribute("successMessage", "회원정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원정보 수정 중 오류가 발생했습니다. 다시 시도해주세요.");
        }

        return "redirect:/mypage/profile"; // 수정 완료 후 회원정보 수정 페이지로 리다이렉트
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

    @PostMapping("updatePassword")
    public String updatePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @SessionAttribute(name = "sessionUser", required = false) SessionUser sessionUser,
                                 RedirectAttributes redirectAttributes) {

        // 로그인 상태 확인
        if (sessionUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        long userId = sessionUser.getId();

        try {
            // 새 비밀번호와 확인 비밀번호가 일치하지 않는 경우 예외 발생
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            }

            // 비밀번호 변경 처리
            myPageService.updatePassword(userId, currentPassword, newPassword);

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            // 사용자 입력 오류 처리
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // 기타 오류 처리
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 변경 중 오류가 발생했습니다. 다시 시도해주세요.");
        }

        // 회원정보 수정 페이지로 리다이렉트
        return "redirect:/mypage/profile";
    }



}
