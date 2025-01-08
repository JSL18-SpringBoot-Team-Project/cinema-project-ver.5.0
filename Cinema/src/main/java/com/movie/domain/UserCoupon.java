package com.movie.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserCoupon {

    private Long userId;
    private Long couponId;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponEndDate;
    private Integer state;

    private Coupons coupons;

}
