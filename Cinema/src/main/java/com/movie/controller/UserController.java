package com.movie.controller;

import com.movie.domain.User;
import com.movie.service.EmailService;
import com.movie.service.UserService;
import com.movie.util.ValidationUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ValidationUtil validationUtil;

    private final Map<String, Boolean> verifiedEmails = new HashMap<>();

    @PostMapping("/checkemail")
    @ResponseBody
    public Map<String, Object> checkEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        boolean exists = userService.isEmailRegistered(email);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        if (exists) {
            response.put("message", "このメールアドレスは既に登録されています。");
        }
        return response;
    }

    @PostMapping("/sendcode")
    @ResponseBody
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (!validationUtil.isValidEmail(email)) {
            return ResponseEntity.badRequest().body("無効なメールアドレスです。");
        }
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok("認証コードが送信されました。");
    }

    @PostMapping("/verifycode")
    @ResponseBody
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> payload, HttpSession session) {
        String email = payload.get("email");
        String code = payload.get("code");
        System.out.println(code);
        if (!validationUtil.isValidEmail(email) || !validationUtil.isValidCode(code)) {
            return ResponseEntity.badRequest().body("無効なデータです。");
        }

        if (emailService.verifyCode(email, code)) {
            session.setAttribute("verifiedEmail", email);
            session.setAttribute("verifiedEmailExpiry", System.currentTimeMillis() + 15 * 60 * 1000);
            return ResponseEntity.ok("認証が成功しました。");
        } else {
            return ResponseEntity.badRequest().body("認証コードが間違っています。");
        }
    }


    @GetMapping("/signup")
    public String signup(HttpSession session, Model model) {
        String email = (String) session.getAttribute("verifiedEmail");
        Long expiryTime = (Long) session.getAttribute("verifiedEmailExpiry");

        // 세션 확인 및 유효성 체크
        if (email == null || expiryTime == null || System.currentTimeMillis() > expiryTime) {
            session.removeAttribute("verifiedEmail");
            session.removeAttribute("verifiedEmailExpiry");
            return "redirect:/";
        }

        model.addAttribute("email", email);
        model.addAttribute("content", "user/signup");
        model.addAttribute("title", "会員登録");
        return "layout/base";
    }

    @PostMapping("/signup")
    public String signup(User user, HttpSession session, Model model) {
        String sessionEmail = (String) session.getAttribute("verifiedEmail");
        boolean hasErrors = false;

        if (!user.getEmail().equals(sessionEmail)) {
            model.addAttribute("error_email", "無効なメールアドレスです。");
            hasErrors = true;
        }
        if (!validationUtil.isValidPassword(user.getPassword())) {
            model.addAttribute("error_password", "パスワードは8~16文字の英数字と特殊文字を組み合わせてください。");
            hasErrors = true;
        }
        if (user.getName().trim().isEmpty()) {
            model.addAttribute("error_name", "名前を入力してください。");
            hasErrors = true;
        }
        if (user.getBirth() != null && !user.getBirth().toString().isEmpty()) {
            if (!validationUtil.isValidBirthdate(user.getBirth().toString())) {
                model.addAttribute("error_birth", "無効な生年月日形式です。");
                hasErrors = true;
            }
        }

        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            if (!validationUtil.isValidPhoneNumber(user.getPhone())) {
                model.addAttribute("error_phone", "無効な電話番号形式です。");
                hasErrors = true;
            }
        }

        if (hasErrors) {
            model.addAttribute("email", sessionEmail);
            model.addAttribute("content", "user/signup");
            model.addAttribute("title", "会員登録");
            return "layout/base";
        }

        userService.register(user);

        session.removeAttribute("verifiedEmail");
        session.removeAttribute("verifiedEmailExpiry");
        return "redirect:/";
    }

}
