package com.movie.controller;

import com.movie.domain.*;
import com.movie.service.*;
import com.movie.util.DateUtil;
import com.movie.util.FileUtil;
import com.movie.util.MovieSchedulesWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final MovieService movieService;
    private final CouponService couponService;
    private final EventService eventService;
    private final ScheduleService scheduleService;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final FileUtil fileUtil;
    private final MovieDetailService movieDetailService;
    private final InquiryService inquiryService;
    private final DateUtil dateUtil;

    @GetMapping("")
    public String admin(Model model) {

        long todaySale = bookingService.getTodaySale();
        long totalSale = bookingService.getTotalSale();
        List<PaymentDetail> paymentList = paymentService.getRecentPayment();

        model.addAttribute("content", "admin/index");
        model.addAttribute("title", "admin");
        model.addAttribute("todaySale", todaySale);
        model.addAttribute("totalSale", totalSale);
        model.addAttribute("payment", paymentList);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @GetMapping("movie/create")
    public String movieCreate(Model model) {

        model.addAttribute("content", "admin/movie/movie_create");
        model.addAttribute("title", "admin-movie-create");

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("movie/create")
    public String insertMovie(@ModelAttribute Movies movies, @RequestParam("poster_img") MultipartFile poster_img) {
        try {
            if (!poster_img.isEmpty()) {

                String filePath = fileUtil.saveFile(poster_img, true);

                movies.setPosterImg(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/movie/create";
        }

        long id = movieService.insertMovie(movies);

        if (id > 0) {
            return "redirect:/admin/movie/detail?movieId=" + movies.getId();
        } else {
            return "redirect:/admin/movie/create";
        }
    }

    @GetMapping("movie/detail")
    public String movieDetail(long movieId, Model model) {

        Movies movies = movieService.movieInfo(movieId);

        model.addAttribute("content", "admin/movie/movie_detail");
        model.addAttribute("title", "admin-movie-detail");
        model.addAttribute("movie", movies);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("movie/detail")
    public String movieDetail(MovieDetails movieDetails, MultipartFile movie_img) {

        try {
            if (!movie_img.isEmpty()) {

                String filePath = fileUtil.saveFile(movie_img, true);

                movieDetails.setMovieImg(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/movie/detail?movieId=" + movieDetails.getMovieId();
        }

        long result = movieDetailService.insertMovieDetail(movieDetails);

        if (result > 0) {
            return "redirect:/admin/movie/set?id=" + movieDetails.getMovieId();
        } else {
            return "redirect:/admin/movie/detail?movieId=" + movieDetails.getMovieId();
        }

    }

    @GetMapping("movie/list")
    public String movieManageList(Model model) {

        List<Movies> movies = movieService.movieManageList();

        model.addAttribute("content", "admin/movie/movie_list");
        model.addAttribute("title", "admin-movie-list");
        model.addAttribute("movies", movies);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @GetMapping("movie/set")
    public String movieSet(@RequestParam("id") long id, Model model) {

        Movies movies = movieService.movieInfo(id);
        List<Schedules> schedules = scheduleService.usedScheduleList();

        model.addAttribute("content", "admin/movie/movie_set");
        model.addAttribute("title", "admin-movie-set");
        model.addAttribute("movie", movies);
        model.addAttribute("schedules", schedules);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("movie/set")
    public String scheduleSet(@ModelAttribute MovieSchedulesWrapper schedulesWrapper) {

        List<Schedules> schedules = schedulesWrapper.getSchedules();

        scheduleService.insertScheduleWithSeat(schedules);

        return "redirect:/admin/movie/list";

    }

    @GetMapping("movie/update")
    public String movieManage(@RequestParam("id") long id, Model model) {

        Movies movie = movieService.movieInfo(id);

        model.addAttribute("content", "admin/movie/movie_update");
        model.addAttribute("title", "admin-movie-update");
        model.addAttribute("movie", movie);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("movie/update")

    public String movieUpdate(Movies movies, MultipartFile poster_img) {

        try {
            if (!poster_img.isEmpty()) {

                String filePath = fileUtil.saveFile(poster_img, true);

                movies.setPosterImg(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/movie/update?id" + movies.getId();
        }

        long result = movieService.updateMovie(movies);

        if (result > 0) {
            return "redirect:/admin/movie/list";
        } else {
            return "redirect:/admin/movie/update?id" + movies.getId();
        }
    }

    @GetMapping("movie/detailUpdate")
    public String movieDetailUpdate(long id, Model model) {

        MovieDTO movie = movieDetailService.getMovieDetail(id);

        model.addAttribute("content", "admin/movie/movie_detail_update");
        model.addAttribute("title", "admin-movie-detail-update");
        model.addAttribute("movie", movie.getMovies());
        model.addAttribute("detail", movie.getMovieDetails());

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("movie/detailUpdate")
    public String movieDetailUpdate(MovieDetails movieDetails, MultipartFile movie_img) {

        try {
            if (!movie_img.isEmpty()) {

                String filePath = fileUtil.saveFile(movie_img, true);

                movieDetails.setMovieImg(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/movie/detailUpdate?movieId=" + movieDetails.getMovieId();
        }

        long result = movieDetailService.insertMovieDetail(movieDetails);

        if (result > 0) {
            return "redirect:/admin/movie/list";
        } else {
            return "redirect:/admin/movie/detailUpdate?movieId=" + movieDetails.getMovieId();
        }

    }

    @PostMapping("movie/delete")
    @ResponseBody
    public String deleteMovie(@RequestParam("id") long id) {

        long result = movieService.deleteMovie(id);

        if (result > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @GetMapping("coupon/create")
    public String couponCreate(Model model) {

        model.addAttribute("content", "admin/coupon/coupon_create");
        model.addAttribute("title", "admin-coupon-create");

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("coupon/create")
    public String insertCoupon(Coupons coupons) {

        long result = couponService.insertCoupon(coupons);

        if (result > 0) {
            return "redirect:/admin/coupon/list";
        } else {
            return "redirect:/admin/coupon/create";
        }
    }

    @GetMapping("coupon/list")
    public String couponList(Model model) {

        List<Coupons> coupons = couponService.couponList();

        model.addAttribute("content", "admin/coupon/coupon_list");
        model.addAttribute("title", "admin-coupon-list");
        model.addAttribute("coupons", coupons);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @GetMapping("coupon/update")
    public String couponManage(@RequestParam("id") long id, Model model) {

        Coupons coupon = couponService.couponDetail(id);

        model.addAttribute("content", "admin/coupon/coupon_update");
        model.addAttribute("title", "admin-coupon-list");
        model.addAttribute("coupon", coupon);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("coupon/update")
    public String updateCoupon(Coupons coupons) {

        long result = couponService.updateCoupon(coupons);

        if (result > 0) {
            return "redirect:/admin/coupon/list";
        } else {
            return "redirect:/admin/coupon/update?id=" + coupons.getId();
        }
    }

    @PostMapping("coupon/delete")
    @ResponseBody
    public String deleteCoupon(@RequestParam("id") long id) {

        long result = couponService.deleteCoupon(id);

        if (result > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @GetMapping("event/create")
    public String eventCreate(Model model) {

        List<Coupons> coupons = couponService.couponList();

        model.addAttribute("content", "admin/event/event_create");
        model.addAttribute("title", "admin-event-create");
        model.addAttribute("coupons", coupons);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("event/create")
    public String insertEvent(@ModelAttribute Events events, @RequestParam("event_img") MultipartFile event_img) {

        try {
            if (!event_img.isEmpty()) {

                String filePath = fileUtil.saveFile(event_img, false);

                events.setEventImg(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/event/create";
        }

        long result = eventService.insertEvent(events);

        if (result > 0) {
            return "redirect:/admin/event/list";
        } else {
            return "redirect:/admin/event/create";
        }

    }

    @GetMapping("event/list")
    public String eventList(Model model) {

        List<Events> events = eventService.eventManageList();

        model.addAttribute("content", "admin/event/event_list");
        model.addAttribute("title", "admin-event-list");
        model.addAttribute("events", events);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @GetMapping("event/update")
    public String eventDetail(@RequestParam("id") long id, Model model) {

        Events events = eventService.eventDetail(id);
        List<Coupons> coupons = couponService.couponList();

        model.addAttribute("content", "admin/event/event_update");
        model.addAttribute("title", "admin-event-update");
        model.addAttribute("event", events);
        model.addAttribute("coupons", coupons);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("event/update")
    public String updateEvent(Events events, MultipartFile event_img) {

        try {
            if (!event_img.isEmpty()) {

                String filePath = fileUtil.saveFile(event_img, false);

                events.setEventImg(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/event/create";
        }

        long result = eventService.updateEvent(events);

        if (result > 0) {
            return "redirect:/admin/event/list";
        } else {

            long id = events.getId();

            return "redirect:/admin/event/update?id=" + id;
        }

    }

    @PostMapping("event/delete")
    @ResponseBody
    public String deleteEvent(@RequestParam("id") long id) {

        long result = eventService.deleteEvent(id);

        if (result > 0) {
            return "success";
        } else {
            return "fail";
        }

    }

    @GetMapping("users")
    public String users(Model model) {

        List<User> users = userService.getUserList();

        model.addAttribute("content", "admin/users/users");
        model.addAttribute("title", "admin-users");
        model.addAttribute("users", users);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("users/delete")
    @ResponseBody
    public String deleteUser(@RequestParam("id") long id) {

        long result = userService.deleteUser(id);

        if (result > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @GetMapping("qna")
    public String qna(@RequestParam(value = "inquiry_type", required = false) String inquiryType,
                      @RequestParam(value = "page", defaultValue = "1") int page,
                      Model model) {
        if (inquiryType != null && inquiryType.isEmpty()) {
            inquiryType = null;
        }

        int pageSize = 10;
        PagingDTO<Inquiries> inquiriesPage = inquiryService.getPagedInquiries(inquiryType, page, pageSize);

        model.addAttribute("inquiries", inquiriesPage.getContent());
        model.addAttribute("currentPage", inquiriesPage.getTotalItems() > 0 ? inquiriesPage.getCurrentPage() : 0);
        model.addAttribute("totalPages", inquiriesPage.getTotalItems() > 0 ? inquiriesPage.getTotalPages() : 0);
        model.addAttribute("totalItems", inquiriesPage.getTotalItems());
        model.addAttribute("selectedType", inquiryType);
        model.addAttribute("inquiryTypes", Arrays.asList(Inquiries.InquiryType.values()));

        model.addAttribute("content", "admin/qna/qna");
        model.addAttribute("title", "admin-qna");

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }

    @PostMapping("qna/answer")
    @ResponseBody
    public ResponseEntity<String> answerInquiry(@RequestBody Map<String, String> payload) {
        try {
            int inquiryId = Integer.parseInt(payload.get("inquiryId"));
            String content = payload.get("content");

            // 답변 등록
            inquiryService.addAnswer(inquiryId, content);

            // 성공 응답
            return ResponseEntity.ok("Answer submitted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit answer");
        }
    }

    @GetMapping("payment")
    public String paymentList(Model model) {

        List<PaymentDetail> paymentList = paymentService.getPaymentList();

        model.addAttribute("content", "admin/payment/payment");
        model.addAttribute("title", "決済リスト");
        model.addAttribute("payment", paymentList);

        List<Inquiries> inquiriesNotAnswerList = inquiryService.getInquiriesNotAnswer();
        String inquiriesNotAnswerCount = inquiryService.getInquiriesNotAnswerCount();
        model.addAttribute("inquiriesNotAnswerList", inquiriesNotAnswerList);
        model.addAttribute("inquiriesNotAnswerCount", inquiriesNotAnswerCount);

        return "admin/layout/admin_base";
    }
}
