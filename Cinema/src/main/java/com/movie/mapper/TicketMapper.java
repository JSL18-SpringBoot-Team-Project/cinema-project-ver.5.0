package com.movie.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TicketMapper {

    @Insert("INSERT INTO tickets (seat_id, booking_id, payment_id, user_id) VALUES (#{seatId}, #{bookingId}, #{paymentId}, #{userId})")
    public void insertTicket(long seatId, long bookingId, String paymentId, long userId);

}
