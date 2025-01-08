package com.movie.controller;

import com.movie.domain.*;
import com.movie.service.*;
import com.movie.util.RandomIdUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired BookingService bookingService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieDetailService movieDetailService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private RandomIdUtil randomIdUtil;

    @Value("${portone.storeId}")
    private String storeId;

    @Value("${portone.channelKey}")
    private String channelKey;


    @PostMapping("/proceed")
    public String bookingProceed(@RequestParam("selectedSeatIds") String selectedSeatId, Model model, @RequestParam("scheduleId") long scheduleId, @ModelAttribute("sessionUser") SessionUser sessionUser) {

        if (sessionUser != null) {
            // 문자열을 Long 배열로 변환
            String[] seatIdStrings = selectedSeatId.split(",");
            Long[] seatIds = Arrays.stream(seatIdStrings)
                    .map(Long::valueOf) // 문자열을 Long으로 변환
                    .toArray(Long[]::new); // Long[] 배열로 변환

            seatService.bookingStates(seatIds);

            User user = userService.getUserInfo(sessionUser.getId());
            List<Seats> seats = seatService.bookingSeats(seatIds);
            Schedules schedule = scheduleService.getSchedule(scheduleId);
            MovieDTO movie = movieDetailService.getMovieDetail(schedule.getMovieId());

            int totalPrice = 0;

            for (Seats seat : seats) {
                totalPrice += seat.getSeatPrice();
            }

            // orderName에 들어갈 seatColumn + seatRow
            List<String> seatNames = seats.stream()
                    .map(s -> String.valueOf(s.getSeatColumn()) + String.valueOf(s.getSeatRow()))
                    .collect(Collectors.toList());


            // customData.item에 들어갈 seat.id
            List<Long> seatIdList = seats.stream()
                    .map(Seats::getId)
                    .collect(Collectors.toList());

            String paymentId = randomIdUtil.makePaymentId();

            model.addAttribute("user", user);
            model.addAttribute("seats", seats);
            model.addAttribute("schedule", schedule);
            model.addAttribute("movie", movie.getMovies());
            model.addAttribute("detail", movie.getMovieDetails());
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("storeId", storeId);
            model.addAttribute("channelKey", channelKey);
            model.addAttribute("paymentId", paymentId);
            model.addAttribute("seatNames", seatNames);
            model.addAttribute("seatIdList", seatIdList);

        } else {
            return "redirect:/";
        }
        return "booking/booking_type";

    }

    /**
     * HTML에서 PortOne 모달로 결제 완료 후,
     * 프런트(자바스크립트)에서 seatsId, paymentId, totalAmount 등을 가지고 POST로 들어옴.
     *
     * 1) 결제금액 검증 (DB에서 seats 가격 합산 + 쿠폰 할인 적용 값과 일치하는지)
     * 2) 문제없다면 seats state = 1(예약됨) 업데이트
     * 3) payment 테이블에 결제 정보 삽입
     * 4) tickets 테이블에 (seat_id, booking_id) 정보 삽입
     * 5) 완료 페이지로 이동 or JSON 응답
     */
    @PostMapping("/pay")
    @ResponseBody
    public Map<String, Object> pay(
            @RequestBody Map<String, Object> payload,  // JSON 데이터를 Map으로 수신
            @ModelAttribute("sessionUser") SessionUser sessionUser,
            HttpSession session
    ) {
        // JSON 데이터를 수신하여 파싱
        List<Object> seatsIdObj = (List<Object>) payload.get("seatsId");
        Long[] seatsId = seatsIdObj.stream()
                .map(obj -> Long.valueOf(obj.toString()))
                .toArray(Long[]::new);

        Long couponId = null;
        if (payload.get("couponId") != null) {
            try {
                couponId = Long.parseLong(payload.get("couponId").toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid couponId format: " + payload.get("couponId"), e);
            }
        }
        String paymentId = payload.get("paymentId").toString();
        int clientPaidAmount = Integer.parseInt(payload.get("totalAmount").toString());
        String orderName = payload.get("orderName").toString();
        Long movieId = Long.parseLong(payload.get("movie").toString());
        Long scheduleId = Long.parseLong(payload.get("schedule").toString());

        // 기존 로직 유지
        int serverCalculatedPrice = (couponId != null) ?
                paymentService.calculateTotalPrice(seatsId, couponId) :
                paymentService.calculateTotalPrice(seatsId);

        String result = "";

        Map<String, Object> response = new HashMap<>();

        if(serverCalculatedPrice != clientPaidAmount) {
            result = "fail";
            response.put("result", result);
            System.out.println("clientPaidAmount = " + clientPaidAmount);
            System.out.println("serverCalculatedPrice = " + serverCalculatedPrice);
            return response;
        } else {
            seatService.paymentStates(seatsId);

            Bookings bookings = (couponId != null) ?
                    bookingService.insertBooking(sessionUser.getId(), scheduleId, couponId, serverCalculatedPrice) :
                    bookingService.insertBooking(sessionUser.getId(), scheduleId, serverCalculatedPrice);

            Payment payment = new Payment();
            payment.setId(paymentId);
            payment.setUserId(sessionUser.getId());
            payment.setBookingId(bookings.getId());
            payment.setOrderName(orderName);
            payment.setTotalAmount(serverCalculatedPrice);

            paymentService.insertPayment(payment);
            ticketService.insertTickets(bookings.getId(), seatsId, paymentId, (long)sessionUser.getId());

            List<Seats> seats = seatService.bookingSeats(seatsId);

            Schedules schedule = scheduleService.getSchedule(scheduleId);

            movieService.updateMovieAudience(movieId);
            Movies movie = movieService.movieInfo(movieId);

            result = "success";

            session.setAttribute("payment", payment);
            session.setAttribute("seats", seats);
            session.setAttribute("schedule", schedule);
            session.setAttribute("movie", movie);

            response.put("result", result);
            System.out.println(result);

            return response;
        }


    }
}
