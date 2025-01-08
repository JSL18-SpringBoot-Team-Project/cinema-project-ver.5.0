package com.movie.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Movies {

    private Long id;
    private String title;
    private Long runningTime;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private Long audience;
    private String genre;
    private String posterImg;

}
