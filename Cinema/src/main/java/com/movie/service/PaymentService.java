package com.movie.service;

import com.movie.domain.Coupons;
import com.movie.domain.Payment;
import com.movie.domain.Seats;
import com.movie.mapper.CouponMapper;
import com.movie.mapper.PaymentMapper;
import com.movie.mapper.SeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Autowired
    private CouponMapper couponMapper;

    public long insertPayment(Payment payment) {
        return paymentMapper.insertPayment(payment);
    }

    public int calculateTotalPrice(Long[] seatsId, Long couponId) {

        int totalAmount = 0;

        for (long seatId : seatsId) {
            Seats seats = seatMapper.checkSeat(seatId);
            totalAmount += seats.getSeatPrice();
        }

        Coupons coupons = couponMapper.couponDetail(couponId);

        int sale = 0;

        if(coupons.getCouponType().equals("P")) {
            sale = (int) ((coupons.getCouponPrice() / 100.0) * totalAmount);
        } else if (coupons.getCouponType().equals("Y")) {
            sale = coupons.getCouponPrice() - totalAmount;
        }

        totalAmount -= sale;

        return totalAmount;

    }

    public int calculateTotalPrice(Long[] seatsId) {

        int totalAmount = 0;

        for (long seatId : seatsId) {
            Seats seats = seatMapper.checkSeat(seatId);
            totalAmount += seats.getSeatPrice();
        }

        return totalAmount;

    }
}
