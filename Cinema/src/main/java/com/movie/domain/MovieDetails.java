package com.movie.domain;

import lombok.Data;

@Data
public class MovieDetails {

    private Long detail_id;
    private Long movieId;
    private String trailer;
    private String movieImg;
    private String country;
    private String production;
    private String distribution;
    private String director;
    private String actor;
    private String content;
}
