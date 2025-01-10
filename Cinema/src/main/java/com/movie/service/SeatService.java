package com.movie.service;

import com.movie.domain.ScreenScheduleSeatDto;
import com.movie.domain.Seats;
import com.movie.mapper.SeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatMapper seatMapper;
    private Map<String, Long[]> sessionSeatsMap = new HashMap<>();
    private Map<String, Long> sessionExpiration = new HashMap<>();

    public ScreenScheduleSeatDto getSeat(long scheduleId) {
        return seatMapper.getSeat(scheduleId);
    }

    public long checkSeatState(List<Long> seatIds) {

        System.out.println("service : " + seatIds);

        long result = 0;

        for (Long id : seatIds) {

            Seats seat = seatMapper.checkSeat(id);

            if (seat.getState() != 0) {
                result = 1;
                System.out.println("if문 : " + result);
                return result;
            }
        }
        System.out.println("not if문: " + result);
        return result;

    }

    public void bookingStates(Long[] seatIds) {

        for (Long id : seatIds) {
            seatMapper.bookingStates(id);
        }

    }

    public void paymentStates(Long[] seatIds, String sessionId) {

        for (Long id : seatIds) {
            seatMapper.paymentStates(id);
        }

        sessionSeatsMap.remove(sessionId);
        sessionExpiration.remove(sessionId);

    }

    public void resetStates(Long[] seatIds) {
        for (Long id : seatIds) {
            seatMapper.resetStates(id);
        }
    }

    public List<Seats> bookingSeats(Long[] seatIds) {

        List<Seats> seats = new ArrayList<>();

        for (long id : seatIds) {
            Seats seat = seatMapper.checkSeat(id);
            seats.add(seat);
        }
        return seats;
    }

    public ScreenScheduleSeatDto getScreenScheduleByScheduleId(long scheduleId) {
        return seatMapper.getScreenScheduleByScheduleId(scheduleId);
    }

    public void storeSessionSeats(String sessionId, Long[] seatIds) {
        sessionSeatsMap.put(sessionId, seatIds);
    }

    public List<String> getExpiredSessionIds() {
        long currentTime = System.currentTimeMillis();
        return sessionExpiration.entrySet().stream().filter(entry -> entry.getValue() < currentTime).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public void setSessionExpiration(String sessionId, long durationInMillis) {
        sessionExpiration.put(sessionId, System.currentTimeMillis() + durationInMillis);
    }

    public Long[] getSeatsBySession(String sessionId) {
        return sessionSeatsMap.getOrDefault(sessionId, new Long[0]);
    }

    public void resetSession(String sessionId) {
        Long[] seatIds = sessionSeatsMap.remove(sessionId);
        if (seatIds != null) {
            resetStates(seatIds);
        }
        sessionExpiration.remove(sessionId);
    }


}
