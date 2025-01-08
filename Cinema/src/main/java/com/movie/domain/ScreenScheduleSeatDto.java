package com.movie.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ScreenScheduleSeatDto {

    private Long scheduleId;
    private Long movieId;
    private LocalDate watchDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String screenName;
    private String screenType;

    private Long seatPrice;

    private List<Seats> seatList;
}
