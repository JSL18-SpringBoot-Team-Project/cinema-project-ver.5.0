package com.movie.service;

import com.movie.domain.MemberCoupon;
import com.movie.domain.User;
import com.movie.domain.UserCoupon;
import com.movie.mapper.CouponMapper;
import com.movie.mapper.TicketMapper;
import com.movie.mapper.UserCouponMapper;
import com.movie.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private TicketMapper ticketMapper;

    public void register(User user) {
        if (isEmailRegistered(user.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertUser(user);

        long userid = (long) user.getId();
        System.out.println("1userid = " + userid);
        eventService.userEvent(userid);
    }

    // OAuth 사용자 처리 (일반 사용자와 구분)
    public void registerSocialUser(User user) {
        if (isEmailRegistered(user.getEmail())) {
            // 이미 등록된 사용자인 경우 업데이트
            userMapper.updateSocialUser(user);
        } else {
            // 새 사용자인 경우 등록
            userMapper.insertSocialUser(user);
        }
    }

    public boolean isEmailRegistered(String email) {
        return userMapper.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public User getUserInfo(long userId) {

        User user = userMapper.getUserInfo(userId);

        List<UserCoupon> coupons = userCouponMapper.getUserCouponsByUserId(userId);

        user.setCoupons(coupons);
        System.out.println("user = " + user);
        return user;
    }

    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    public long deleteUser(long id) {
        return userMapper.deleteUser(id);
    }

    // 비밀번호 확인
    public boolean verifyPassword(long userId, String password) {
        String hashedPassword = userMapper.getPassword(userId);
        if (hashedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(password, hashedPassword);
    }

    // 회원정보 수정
    public void updateUserInfo(User user) {
        userMapper.updateSocialUser(user);
    }

    // 비밀번호 변경 (기존 비밀번호 제한)
    public void changePassword(long userId, String currentPassword, String newPassword) {
        User user = userMapper.getUserInfo(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호가 기존 비밀번호와 동일한지 확인
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        // 새 비밀번호 암호화 및 저장
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updatePassword(user);
    }

    // 비밀번호 복잡성 검증
    private boolean isPasswordComplex(String password) {
        if (password == null || password.length() < 10) {
            return false;
        }

        int count = 0;
        if (password.matches(".*[A-Za-z].*")) count++; // 영문 포함
        if (password.matches(".*[0-9].*")) count++; // 숫자 포함
        if (password.matches(".*[!@#$%^&*()_+=<>?/{}~`\\-].*")) count++; // 특수문자 포함

        return count >= 2; // 최소 2가지 포함
    }


    // 사용자 정보 수정 메서드
    public void updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("유효한 사용자 정보가 필요합니다.");
        }
        userMapper.updateUser(user);
    }


    public boolean isEmailRegisteredWithProvider(String email, String provider) {
        return userMapper.existsByEmailAndProvider(email, provider);
    }

    public void updatePassword(String email, String newPassword) {
        String hashedPassword = passwordEncoder.encode(newPassword);
        userMapper.updatePassword2(email, hashedPassword);
    }
}
