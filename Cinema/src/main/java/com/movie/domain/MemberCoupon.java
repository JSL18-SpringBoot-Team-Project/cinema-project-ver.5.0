package com.movie.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberCoupon {

    private long id;
    private String eventTitle;
    private String eventContent;
    private String eventImg;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;
    private long couponId;
    private long userId;
    private String couponPrice;

}
