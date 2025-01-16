package com.movie.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Bookings {

    private Long id;
    private Long userId;
    private Long scheduleId;
    private LocalDateTime bookingTimestamp; // 예약일
    private LocalDateTime cancelTimestamp;  // 취소일
    private Long couponId;
    private Long price;
    private Long state;
    private String title; // 영화 제목
    private LocalDate watchDate; // 상영일
    private Integer runningTime;
    private String seatNumber;    // 좌석 번호

}
