package com.movie.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<Seats> seats; // 좌석 정보 리스트

}
