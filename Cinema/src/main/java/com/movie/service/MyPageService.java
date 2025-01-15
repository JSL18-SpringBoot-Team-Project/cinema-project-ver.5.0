package com.movie.service;

import com.movie.domain.Bookings;
import com.movie.domain.Inquiries;
import com.movie.domain.PagingDTO;
import com.movie.domain.UserCoupon;
import com.movie.mapper.MyPageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyPageService {

    private final MyPageMapper myPageMapper;
    private final PagingService pagingService;

    @Autowired
    public MyPageService(MyPageMapper myPageMapper) {
        this.myPageMapper = myPageMapper;
        this.pagingService = new PagingService();
    }

    // 페이징 예매내역 조회 처리
    public PagingDTO<Bookings> getBookingList(long userId, int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize; // OFFSET 계산
            List<Bookings> bookings = myPageMapper.getBookingList(userId, pageSize, offset);
            long totalCount = myPageMapper.getTotalBookingCount(userId);

            if (bookings == null || bookings.isEmpty()) {
                return new PagingDTO<>(List.of(), page, 0, 0, pageSize); // 빈 리스트 반환
            }

            return pagingService.createPaging(bookings, totalCount, page, pageSize);
        } catch (Exception e) {
            throw new RuntimeException("예매 내역을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 영화 제목으로 페이징 예매내역 조회 처리
    public PagingDTO<Bookings> getBookingListByTitle(long userId, String title, int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize; // OFFSET 계산
            List<Bookings> bookings = myPageMapper.getBookingListByTitle(userId, title, pageSize, offset);
            long totalCount = myPageMapper.getTotalBookingCount(userId); // 제목에 따른 카운트는 필요하면 별도 쿼리 작성

            if (bookings == null || bookings.isEmpty()) {
                return new PagingDTO<>(List.of(), page, 0, 0, pageSize); // 빈 리스트 반환
            }

            return pagingService.createPaging(bookings, totalCount, page, pageSize);
        } catch (Exception e) {
            throw new RuntimeException("영화 제목으로 예매 내역을 조회하는 중 오류가 발생했습니다.", e);
        }
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

    // 사용자 문의 내역 조회
    public List<Inquiries> getInquiries(long userId, String status, String keyword) {
        // "all" 상태는 필터링 없이 처리
        if ("all".equals(status)) {
            status = null;
        }

        try {
            return myPageMapper.getInquiries(userId, status, keyword);
        } catch (Exception e) {
            throw new RuntimeException("사용자 문의 리스트 조회 중 오류가 발생했습니다.", e);
        }
    }
}
