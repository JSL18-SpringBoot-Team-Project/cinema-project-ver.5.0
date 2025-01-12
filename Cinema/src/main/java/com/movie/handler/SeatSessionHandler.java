package com.movie.handler;

import com.movie.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class SeatSessionHandler {

    @Autowired
    private final SeatService seatService;

    public SeatSessionHandler(SeatService seatService) {
        this.seatService = seatService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkExpiredSessions() {
        List<String> expiredSessionIds = seatService.getExpiredSessionIds();

        for (String sessionId : expiredSessionIds) {
            try {
                Long[] seatIds = seatService.getSeatsBySession(sessionId);

                if (seatIds != null && seatIds.length > 0) {
                    seatService.resetStates(seatIds);
                }

                seatService.resetSession(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
