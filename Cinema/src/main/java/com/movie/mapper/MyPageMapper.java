package com.movie.mapper;

import com.movie.domain.Bookings;
import com.movie.domain.Inquiries;
import com.movie.domain.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MyPageMapper {

    // 사용자 ID를 기반으로 예매 내역 조회
    List<Bookings> getBookingList(@Param("userId") long userId,
                                  @Param("pageSize") int pageSize,
                                  @Param("offset") int offset);

    // 사용자 ID와 영화 제목을 기반으로 예매 내역 조회
    List<Bookings> searchBookingsByTitle(@Param("userId") long userId,
                                         @Param("title") String title,
                                         @Param("pageSize") int pageSize,
                                         @Param("offset") int offset);

    // 사용자 ID를 기반으로 취소 내역 조회
    List<Bookings> getCancelList(@Param("userId") long userId,
                                 @Param("pageSize") int pageSize,
                                 @Param("offset") int offset);

    // 총 예매 내역 수 조회
    int getTotalBookingCount(@Param("userId") long userId);

    // 총 취소 내역 수 조회
    int getTotalCancelCount(@Param("userId") long userId);

    // 사용자 보유 예매 티켓 조회
    @Select("SELECT COUNT(*) FROM tickets WHERE user_id = #{userId}")
    int getTicketCount(@Param("userId") long userId);

    // 사용자 보유 쿠폰 개수 조회
    @Select("SELECT COUNT(*) FROM user_coupons WHERE user_id = #{userId} AND state = 1")
    int getCouponCount(@Param("userId") long userId);

    // 사용자 쿠폰 리스트 조회
    List<UserCoupon> getUserCouponList(@Param("userId") long userId, @Param("filter") String filter);

    // 사용자 ID를 기반으로 문의 내역 조회
    List<Inquiries> getInquiries(
            @Param("userId") long userId,
            @Param("status") String status,
            @Param("keyword") String keyword
    );


    List<Bookings> getBookingList(Map<String, Object> params);

    List<Bookings> searchBookingsByTitle(Map<String, Object> params);

    List<Bookings> getCancelList(Map<String, Object> params);
}
