package com.movie.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketMapper {

    @Insert("INSERT INTO tickets (seat_id, booking_id, payment_id, user_id) VALUES (#{seatId}, #{bookingId}, #{paymentId}, #{userId})")
    public void insertTicket(long seatId, long bookingId, String paymentId, long userId);

    int getTicketCountByUserId(long userId);
}
