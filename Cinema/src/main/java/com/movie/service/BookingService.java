package com.movie.service;

import com.movie.domain.Bookings;
import com.movie.domain.Seats;
import com.movie.mapper.BookingMapper;
import com.movie.mapper.TicketMapper;
import com.movie.mapper.UserCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private TicketMapper ticketMapper;

    // 쿠폰과 함께 예매
    public Bookings insertBooking(long userId, long scheduleId, long couponId, long totalPrice) {

        Bookings bookings = new Bookings();

        bookings.setUserId(userId);
        bookings.setScheduleId(scheduleId);
        bookings.setCouponId(couponId);
        bookings.setPrice(totalPrice);


        bookingMapper.insertBookingWithCoupon(bookings); // 예매 데이터 저장
        userCouponMapper.updateCouponState(userId, couponId); // 쿠폰 상태 업데이트

        return bookings; // 좌석 데이터 저장
    }

    // 쿠폰 없이 예매
    public Bookings insertBooking(long userId, long scheduleId, long totalPrice) {

        Bookings bookings = new Bookings();

        bookings.setUserId(userId);
        bookings.setScheduleId(scheduleId);
        bookings.setPrice(totalPrice);

        bookingMapper.insertBooking(bookings);

        return bookings;
    }

    // 사용자 ID를 기반으로 예매 내역 조회
    public List<Bookings> getBookingList(long userId) {
        return bookingMapper.getBookingList(userId);
    }

    // 영화 제목 필터링
    public List<Bookings> searchBookingsByTitle(long userId, String title) {
        return bookingMapper.searchBookingsByTitle(userId, title);
    }

    // 사용자 ID를 기반으로 취소 내역 조회
    public List<Bookings> getCancelList(long userId) {
        return bookingMapper.getCancelList(userId);
    }

    public long getTodaySale() {
        return bookingMapper.getTodaySale();
    }

}
