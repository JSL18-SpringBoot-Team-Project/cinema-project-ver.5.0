package com.movie.service;

import com.movie.domain.Schedules;
import com.movie.domain.ScreenScheduleSeatDto;
import com.movie.domain.Seats;
import com.movie.mapper.ScheduleMapper;
import com.movie.mapper.SeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    @Autowired
    private final ScheduleMapper scheduleMapper;
    private final SeatMapper seatMapper;

    public void insertScheduleWithSeat(List<Schedules> schedules) {

        for (Schedules schedule : schedules) {

            long id = scheduleMapper.insertSchedule(schedule);
            id = schedule.getId();
            int screenId = schedule.getScreenId();
            long seatPrice = getPriceByScreen(screenId);

            List<Seats> seats = new ArrayList<>();
            char[] seatColumns = {'A', 'B', 'C', 'D'};

            for (char column : seatColumns) {
                for (long row = 1; row <= 23; row++) {
                    Seats seat = new Seats();

                    seat.setScreenId(screenId);
                    seat.setScheduleId(id);
                    seat.setSeatColumn(column);
                    seat.setSeatRow(row);
                    seat.setSeatPrice(seatPrice);

                    seats.add(seat);
                }
            }

            seatMapper.insertSeat(seats);

        }

    }

    private long getPriceByScreen(int screenId) {

        switch (screenId) {
            case 1:
                return 1000;
            case 2:
                return 1200;
            case 3:
                return 1500;
            case 4:
                return 2000;
            default:
                throw new IllegalArgumentException("Invalid screen ID: " + screenId);
        }

    }

    public List<Schedules> schedulesList() {
        return scheduleMapper.scheduleList();
    }

    public List<ScreenScheduleSeatDto> movieSchedulesList(long movieId) {
        return scheduleMapper.movieScheduleList(movieId);
    }

    public List<Schedules> usedScheduleList() {
        return scheduleMapper.usedScheduleList();
    }

    public Schedules getSchedule(long id) {
        return scheduleMapper.getSchedule(id);
    }

}
