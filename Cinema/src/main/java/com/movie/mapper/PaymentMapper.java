package com.movie.mapper;

import com.movie.domain.Payment;
import com.movie.domain.PaymentDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {

    @Insert("INSERT INTO payment (id, user_id, booking_id, order_name, total_amount) VALUES (#{id}, #{userId}, #{bookingId}, #{orderName}, #{totalAmount})")
    public long insertPayment(Payment payment);

    public List<PaymentDetail> getRecentPayment();

    public List<PaymentDetail> getPaymentList();
}
