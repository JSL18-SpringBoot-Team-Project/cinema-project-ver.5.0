package com.movie.service;

import com.movie.domain.Bookings;
import com.movie.domain.PagingDTO;
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
    public PagingDTO<Bookings> getBookingList(long userId, Integer lastBookingTimestamp, int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize;

            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("lastBookingTimestamp", lastBookingTimestamp);
            params.put("pageSize", pageSize);
            params.put("offset", offset);

            List<Bookings> bookings = myPageMapper.getBookingList(params);
            long totalCount = myPageMapper.getTotalBookingCount(params);

            return pagingService.createPaging(bookings, totalCount, page, pageSize);
        } catch (Exception e) {
            throw new RuntimeException("예매 내역을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 영화 제목으로 페이징 예매내역 조회 처리
    public PagingDTO<Bookings> searchBookingsByTitle(long userId, String title, Integer lastBookingTimestamp, int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize;

            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("title", title);
            params.put("lastBookingTimestamp", lastBookingTimestamp);
            params.put("pageSize", pageSize);
            params.put("offset", offset);

            List<Bookings> bookings = myPageMapper.searchBookingsByTitle(params);
            long totalCount = myPageMapper.getTotalBookingCount(params);

            return pagingService.createPaging(bookings, totalCount, page, pageSize);
        } catch (Exception e) {
            throw new RuntimeException("영화 제목으로 예매 내역을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 페이징 취소 내역 조회
    public PagingDTO<Bookings> getCancelList(long userId, Integer lastCancelTimestamp, int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize;

            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("lastCancelTimestamp", lastCancelTimestamp);
            params.put("pageSize", pageSize);
            params.put("offset", offset);

            List<Bookings> cancelList = myPageMapper.getCancelList(params);
            long totalCount = myPageMapper.getTotalCancelCount(params.size());

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

}
