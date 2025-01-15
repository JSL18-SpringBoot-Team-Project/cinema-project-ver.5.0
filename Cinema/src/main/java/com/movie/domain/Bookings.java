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
    private LocalTime startTime; // 상영 시작 시간
    private LocalTime endTime;   // 상영 종료 시간
    private String seatNumber;    // 좌석 번호

}
