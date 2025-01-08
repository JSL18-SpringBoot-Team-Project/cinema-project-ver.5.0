package com.movie.domain;

import lombok.Data;

@Data
public class Payment {

    private String id;
    private Integer userId;
    private Long bookingId;
    private String orderName;
    private int totalAmount;
}
