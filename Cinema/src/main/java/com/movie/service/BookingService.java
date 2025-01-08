package com.movie.service;

import com.movie.domain.Bookings;
import com.movie.domain.Seats;
import com.movie.mapper.BookingMapper;
import com.movie.mapper.TicketMapper;
import com.movie.mapper.UserCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    public Bookings insertBooking(long userId, long scheduleId, long couponId, long totalPrice) {

        Bookings bookings = new Bookings();

        bookings.setUserId(userId);
        bookings.setScheduleId(scheduleId);
        bookings.setCouponId(couponId);
        bookings.setPrice(totalPrice);

        bookingMapper.insertBookingWithCoupon(bookings);
        userCouponMapper.updateCouponState(userId, couponId);

        return bookings;
    }

    public Bookings insertBooking(long userId, long scheduleId, long totalPrice) {

        Bookings bookings = new Bookings();

        bookings.setUserId(userId);
        bookings.setScheduleId(scheduleId);
        bookings.setPrice(totalPrice);

        bookingMapper.insertBooking(bookings);

        return bookings;
    }
}
