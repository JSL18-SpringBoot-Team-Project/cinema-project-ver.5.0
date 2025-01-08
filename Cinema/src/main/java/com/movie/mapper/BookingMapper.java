package com.movie.mapper;

import com.movie.domain.Bookings;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface BookingMapper {

    public void insertBookingWithCoupon(Bookings bookings);

    public void insertBooking(Bookings bookings);

}
