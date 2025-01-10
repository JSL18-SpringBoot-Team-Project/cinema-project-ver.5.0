package com.movie.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Bookings {

    private Long id;
    private Long userId;
    private Long scheduleId;
    private LocalDateTime bookingTimestamp;
    private LocalDateTime cancelTimestamp;
    private Long couponId;
    private Long price;
    private Long state;
    private String title;
    private List<Long> seatId;
}
