package com.movie.domain;

import lombok.Data;

@Data
public class Seats {

    private Long id;
    private Integer screenId;
    private Long scheduleId;
    private char seatColumn;
    private Long seatRow;
    private Long seatPrice;
    private Integer state;

}
