package com.movie.mapper;

import com.movie.domain.Bookings;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookingMapper {

    // 쿠폰과 함께 예매
    public void insertBookingWithCoupon(Bookings bookings);

    // 쿠폰 없이 예매
    public void insertBooking(Bookings bookings);

    // 사용자 ID를 기반으로 예매 내역 조회
    List<Bookings> getBookingList(@Param("userId") Long userId);

    List<Bookings> searchBookingsByTitle(@Param("userId") long userId, @Param("title") String title);

    // 사용자 ID를 기반으로 취소 내역 조회
    List<Bookings> getCancelList(@Param("userId") Long userId);

}
