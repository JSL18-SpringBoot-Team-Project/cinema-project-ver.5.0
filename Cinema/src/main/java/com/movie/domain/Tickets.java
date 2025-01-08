package com.movie.domain;

import lombok.Data;

@Data
public class Tickets {

    private Long id;
    private Long seatId;
    private Long bookingId;
    private String paymentId;
    private Long userId;

}
