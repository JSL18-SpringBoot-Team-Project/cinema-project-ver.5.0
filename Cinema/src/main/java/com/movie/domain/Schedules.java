package com.movie.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Schedules {

    private Long id;
    private Integer screenId;
    private Long movieId;
    private LocalDate watchDate;
    private LocalTime startTime;
    private LocalTime endTime;

}
