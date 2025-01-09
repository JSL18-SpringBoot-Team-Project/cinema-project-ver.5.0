package com.movie.service;

import com.movie.domain.*;
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
            sale = coupons.getCouponPrice();
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

    public List<PaymentDetail> getRecentPayment() {

        List<PaymentDetail> paymentDetails = paymentMapper.getRecentPayment();

        for (PaymentDetail paymentDetail : paymentDetails) {
            if(paymentDetail.getTitle().length() > 15) {
                paymentDetail.setTitle(paymentDetail.getTitle().substring(0, 15) + "…");
            }
            int count = paymentDetail.getOrderName().split(",").length - 1;
            if (count > 2) {
                String[] orderNames = paymentDetail.getOrderName().split(",\\s*"); // 쉼표와 공백으로 분리
                StringBuilder truncatedOrderName = new StringBuilder();

                // 처음 2개의 요소만 추가
                for (int i = 0; i < 2; i++) {
                    truncatedOrderName.append(orderNames[i]);
                    if (i < 1) {
                        truncatedOrderName.append(", ");
                    }
                }
                truncatedOrderName.append("⋯"); // 마지막에 "…" 추가
                paymentDetail.setOrderName(truncatedOrderName.toString());
            }
        }

        return paymentDetails;
    }

    public List<PaymentDetail> getPaymentList() {

        List<PaymentDetail> paymentDetails = paymentMapper.getPaymentList();

        for (PaymentDetail paymentDetail : paymentDetails) {
            if(paymentDetail.getTitle().length() > 15) {
                paymentDetail.setTitle(paymentDetail.getTitle().substring(0, 15) + "⋯");
            }
            int count = paymentDetail.getOrderName().split(",").length - 1;
            if (count > 4) {
                String[] orderNames = paymentDetail.getOrderName().split(",\\s*"); // 쉼표와 공백으로 분리
                StringBuilder truncatedOrderName = new StringBuilder();

                // 처음 4개의 요소만 추가
                for (int i = 0; i < 4; i++) {
                    truncatedOrderName.append(orderNames[i]);
                    if (i < 4) {
                        truncatedOrderName.append(", ");
                    }
                }
                truncatedOrderName.append("⋯"); // 마지막에 "…" 추가
                paymentDetail.setOrderName(truncatedOrderName.toString());
            }
        }

        return paymentDetails;
    }
}
