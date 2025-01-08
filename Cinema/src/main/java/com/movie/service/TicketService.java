package com.movie.service;

import com.movie.mapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    public void insertTickets(Long bookingId, Long[] seatsId, String paymentId, Long userId) {

        for (Long seatId : seatsId) {
            ticketMapper.insertTicket(seatId, bookingId, paymentId, userId);
        }

    }

}
