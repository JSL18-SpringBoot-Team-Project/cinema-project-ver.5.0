package com.movie.mapper;

import com.movie.domain.Bookings;
import com.movie.domain.Inquiries;
import com.movie.domain.User;
import com.movie.domain.UserCoupon;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MyPageMapper {


    // 사용자 ID를 기반으로 취소 내역 조회
    List<Bookings> getCancelList(@Param("userId") long userId,
                                 @Param("pageSize") int pageSize,
                                 @Param("offset") int offset);

    // 총 예매 내역 수 조회
    long getTotalBookingCount(@Param("userId") long userId);

    // 영화 제목으로 총 예매 내역 수 조회
    long getTotalSearchBookingCount(@Param("userId") long userId, @Param("title") String title);

    // 총 취소 내역 수 조회
    long getTotalCancelCount(@Param("userId") long userId);

    // 사용자 보유 예매 티켓 조회
    int getTicketCount(@Param("userId") long userId);

    // 사용자 보유 쿠폰 개수 조회
    int getCouponCount(@Param("userId") long userId);

    // 사용자 쿠폰 리스트 조회
    List<UserCoupon> getUserCouponList(@Param("userId") long userId, @Param("filter") String filter);


    long getInquiryCount(@Param("userId") long userId,
                         @Param("status") String status,
                         @Param("keyword") String keyword);

    @Select("SELECT\n" +
            "    b.booking_timestamp AS bookingTimestamp, \n" +
            "    m.title AS title, \n" +
            "    s.watch_date AS watchDate, \n" +
            "    m.running_time AS runningTime,\n" +
            "    p.order_name AS seatNumber,\n" +
            "    b.price AS price\n" +
            "FROM bookings b\n" +
            "JOIN schedules s ON b.schedule_id = s.id \n" +
            "JOIN movies m ON s.movie_id = m.id \n" +
            "JOIN payment p ON b.id = p.booking_id \n" +
            "WHERE b.user_id = 4 \n" +
            "ORDER BY b.booking_timestamp DESC LIMIT 5 offset 0;")
    @Results({
            @Result(property = "bookingTimestamp", column = "bookingTimestamp"),
            @Result(property = "title", column = "title"),
            @Result(property = "watchDate", column = "watchDate"),
            @Result(property = "runningTime", column = "runningTime"),
            @Result(property = "seatNumber", column = "seatNumber"),
            @Result(property = "price", column = "price")
    })
    List<Bookings> getIndexBookingList(Long userId);

    @Select("select inquiry_type, SUBSTR(content, 1, 20) as content, status, created_at FROM inquiries WHERE user_id = #{userId} ORDER BY created_at DESC")
    @Results({
            @Result(property = "inquiry_type", column = "inquiry_type"),
            @Result(property = "content", column = "content"),
            @Result(property = "status", column = "status"),
            @Result(property = "created_at", column = "created_at")
    })
    List<Inquiries> indexInquiry(Integer userId);

    @Select("SELECT " +
            "b.booking_timestamp AS bookingTimestamp, " +
            "m.title AS title, " +
            "s.watch_date AS watchDate, " +
            "m.running_time AS runningTime, " +
            "p.order_name AS seatNumber, " +
            "b.price AS price " +
            "FROM bookings b " +
            "JOIN schedules s ON b.schedule_id = s.id " +
            "JOIN movies m ON s.movie_id = m.id " +
            "JOIN payment p ON b.id = p.booking_id " +
            "WHERE b.user_id = #{userId} " +
            "AND (#{title} IS NULL OR m.title LIKE CONCAT('%', #{title}, '%')) " +
            "GROUP BY b.id " +
            "ORDER BY b.booking_timestamp DESC")
    @Results({
            @Result(property = "bookingTimestamp", column = "bookingTimestamp"),
            @Result(property = "title", column = "title"),
            @Result(property = "watchDate", column = "watchDate"),
            @Result(property = "runningTime", column = "runningTime"),
            @Result(property = "seatNumber", column = "seatNumber"),
            @Result(property = "price", column = "price")
    })
    List<Bookings> getBookingListByTitle(@Param("userId") Long userId, @Param("title") String title);


    @Delete("DELETE FROM inquiries WHERE id = #{id}")
    void deleteInquiry(@Param("id") Integer id);;

    // 사용자 삭제
    @Delete("DELETE FROM users where id = #{id}")
    public long deleteUser(long id);

    // 사용자 조회
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(long id);

    // 비밀번호 조회
    @Select("SELECT password FROM users WHERE id = #{id}")
    String getPasswordById(long id);

    // 회원정보 수정
    @Update("UPDATE users SET name = #{name}, email = #{email}, phone = #{phone}, birth = #{birth}, updated_at = NOW() WHERE id = #{id}")
    int updateUser(User user);

    // 비밀번호 업데이트
    @Update("UPDATE users SET password = #{password}, last_password_change = NOW(), updated_at = NOW() WHERE id = #{id}")
    int updatePassword(@Param("id") long id, @Param("password") String password);



}
