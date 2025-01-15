package com.movie.mapper;

import com.movie.domain.Bookings;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookingMapper {

    // 쿠폰과 함께 예매
    public void insertBookingWithCoupon(Bookings bookings);

    // 쿠폰 없이 예매
    public void insertBooking(Bookings bookings);

    long getTodaySale();

    long getTotalSale();

}
