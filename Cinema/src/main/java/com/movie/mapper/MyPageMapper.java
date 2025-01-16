package com.movie.mapper;

import com.movie.domain.Bookings;
import com.movie.domain.Inquiries;
import com.movie.domain.User;
import com.movie.domain.UserCoupon;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MyPageMapper {

    // 사용자 ID를 기반으로 예매 내역 조회
    List<Bookings> getBookingList(@Param("userId") long userId,
                                  @Param("pageSize") int pageSize,
                                  @Param("offset") int offset);

    // 사용자 ID와 영화 제목을 기반으로 예매 내역 조회
    List<Bookings> getBookingListByTitle(@Param("userId") long userId,
                                         @Param("title") String title,
                                         @Param("pageSize") int pageSize,
                                         @Param("offset") int offset);

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

    // 사용자 ID를 기반으로 문의 내역 조회
    List<Inquiries> getInquiries(
            @Param("userId") long userId,
            @Param("status") String status,
            @Param("keyword") String keyword
    );

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
