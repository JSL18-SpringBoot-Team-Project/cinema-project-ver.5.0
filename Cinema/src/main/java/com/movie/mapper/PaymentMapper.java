package com.movie.mapper;

import com.movie.domain.Payment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    @Insert("INSERT INTO payment (id, user_id, booking_id, order_name, total_amount) VALUES (#{id}, #{userId}, #{bookingId}, #{orderName}, #{totalAmount})")
    public long insertPayment(Payment payment);
}
