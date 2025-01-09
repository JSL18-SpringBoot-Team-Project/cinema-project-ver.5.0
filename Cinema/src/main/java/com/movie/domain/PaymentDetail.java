package com.movie.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PaymentDetail {

    private String paymentId;
    private String watchDate;
    private String startTime;
    private String orderName;
    private String title;
    private Long totalAmount;

}
