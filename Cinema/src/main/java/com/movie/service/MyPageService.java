package com.movie.service;

import com.movie.domain.*;
import com.movie.mapper.MyPageMapper;
import com.movie.mapper.UserMapper;
import com.nimbusds.common.contenttype.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyPageService {

    private final MyPageMapper myPageMapper;
    private final PagingService pagingService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyPageService(MyPageMapper myPageMapper) {
        this.myPageMapper = myPageMapper;
        this.pagingService = new PagingService();
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return "";
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return false;
            }
        };
    }

    // Index 예매내역 조회
    public List<Bookings> getIndexBookingList(Long userId) {
        return myPageMapper.getIndexBookingList(userId);
    }

    // Index 문의내역 조회
    public List<Inquiries> indexInquiry(Integer userId) {
        return myPageMapper.indexInquiry(userId);
    }

    // 예매내역(영화 제목 검색)
    public List<Bookings> getBookingListByTitle(Long userId, String title) {
        String movieTitle = (title == null || title.trim().isEmpty()) ? null : title.trim();

        return myPageMapper.getBookingListByTitle(userId, movieTitle);
    }

    // 페이징 취소 내역 조회
    public PagingDTO<Bookings> getCancelList(long userId, int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize; // OFFSET 계산
            List<Bookings> cancelList = myPageMapper.getCancelList(userId, pageSize, offset);
            long totalCount = myPageMapper.getTotalCancelCount(userId);

            return pagingService.createPaging(cancelList, totalCount, page, pageSize);
        } catch (Exception e) {
            throw new RuntimeException("예매 취소 내역을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    public int getTicketCount(long userId) {
        return myPageMapper.getTicketCount(userId);
    }

    public int getCouponCount(long userId) {
        return myPageMapper.getCouponCount(userId);
    }
    // 사용자 쿠폰 리스트 조회

    public List<UserCoupon> getUserCouponList(long userId, String filter) {
        // 기본값 설정: 필터가 null이거나 "all"이면 전체 조회
        if (filter == null || "all".equals(filter)) {
            filter = null; // MyBatis에서 조건을 걸지 않도록 null로 처리
        }

        try {
            return myPageMapper.getUserCouponList(userId, filter);
        } catch (Exception e) {
            throw new RuntimeException("사용자 쿠폰 정보를 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 사용자 삭제
    public void deleteUser(long id) {
        long deletedRows = myPageMapper.deleteUser(id);
        if (deletedRows == 0) {
            throw new IllegalArgumentException("ユーザーが存在しません。");
        }
    }

    // 사용자 조회
    public User getUserById(long id) {
        return myPageMapper.getUserById(id);
    }

    // 비밀번호 검증
    public boolean verifyPassword(long userId, String password) {
        String hashedPassword = myPageMapper.getPasswordById(userId);
        if (hashedPassword == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        return passwordEncoder.matches(password, hashedPassword);
    }

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

    // 회원정보 수정
    public void updateUser(User user) {
        int updatedRows = myPageMapper.updateUser(user);
        if (updatedRows == 0) {
            throw new IllegalArgumentException("회원정보 수정에 실패했습니다.");
        }
    }

    public void updatePassword(long userId, String currentPassword, String newPassword) {
        // 기존 비밀번호 확인
        String hashedPassword = myPageMapper.getPasswordById(userId);
        if (hashedPassword == null || !passwordEncoder.matches(currentPassword, hashedPassword)) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 복잡성 검증
        if (!isPasswordComplex(newPassword)) {
            throw new IllegalArgumentException("새 비밀번호는 최소 10자 이상이고, 영문, 숫자, 특수문자 중 두 가지 이상을 포함해야 합니다.");
        }

        // 새 비밀번호 암호화 및 저장
        String encodedPassword = passwordEncoder.encode(newPassword);
        int updatedRows = myPageMapper.updatePassword(userId, encodedPassword);
        if (updatedRows == 0) {
            throw new IllegalArgumentException("비밀번호 변경에 실패했습니다.");
        }
    }


}
