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


    /**
     * 예매 내역 조회 (기본값 처리 포함)
     *
     * @param userId 사용자 ID
     * @param type 예매 타입
     *             current : 현재 이후 상영 예정 예매 내역
     *             past : 상영 종료 이후 예매 내역
     *             all : 모든
     * @param startDate 조회 시작 날짜
     * @param endDate 조회 종료 날짜
     */
    public List<Map<String, Object>> getBookingsByUserId(long userId, String type, String startDate, String endDate) {
        // 기본값 처리
        type = (type == null || type.isEmpty()) ? "all" : type; // type이 null이거나 비어있으면 "all"로 설정. 그렇지 않으면 전달받은 type 사용.
        startDate = (startDate == null || startDate.isEmpty()) ? "2000-01-01" : startDate; // startDate가 null이거나 비어있으면 기본값인 "2000-01-01"로 설정.
        endDate = (endDate == null || endDate.isEmpty()) ? LocalDate.now().toString() : endDate; // endDate가 null이거나 비어있으면 현재 날짜로 설정.

        return bookingMapper.getBookingsByUserId(userId, type, startDate, endDate);
    }

    /**
     * 예매 내역 조회 (기본값 포함, 전체 내역)
     *
     * @param userId 사용자 ID
     * @return 예매 내역 리스트
     */
    public List<Map<String, Object>> getBookingsByUserId(long userId) {
        return getBookingsByUserId(userId, "all", "2000-01-01", LocalDate.now().toString());
    }

    /**
     * 취소 내역 조회
     *
     * @param userId 사용자 ID
     * @return 취소 내역 리스트
     */
    public List<Map<String, Object>> getCancellationList(long userId) {
        return bookingMapper.getCancellationList(userId);
    }

    public long getTodaySale() {
        return bookingMapper.getTodaySale();
    }

    public long getTotalSale() {
        return bookingMapper.getTotalSale();
    }
}
