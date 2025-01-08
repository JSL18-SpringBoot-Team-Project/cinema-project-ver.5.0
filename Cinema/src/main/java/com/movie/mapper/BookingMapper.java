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

    public void insertBookingWithCoupon(Bookings bookings);

    public void insertBooking(Bookings bookings);

    // 예매내역 조회
    List<Map<String, Object>> getBookingsByUserId(@Param("userId") long userId,
                                                  @Param("type") String type,
                                                  @Param("startDate") String startDate,
                                                  @Param("endDate") String endDate);

    List<Map<String, Object>> getBookingsByUserId(@Param("userId") long userId);

    // 예매취소 내역 조회
    List<Map<String, Object>> getCancellationList(@Param("userId") long userId);

    // 날짜 선택
    List<Map<String, Object>> getDateOptions(@Param("userId") long userId);

}
