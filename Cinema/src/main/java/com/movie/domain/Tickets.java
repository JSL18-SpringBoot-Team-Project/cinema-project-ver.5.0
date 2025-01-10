package com.movie.domain;

import lombok.Data;

@Data
public class Tickets {

    private Long id;
    private Long ticketStatus;
    private Long seatId;
    private Long bookingId;
    private String paymentId;
    private Long userId;

}
