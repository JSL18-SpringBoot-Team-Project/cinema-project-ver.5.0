package com.movie.mapper;

import com.movie.domain.MemberCoupon;
import com.movie.domain.UserCoupon;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserCouponMapper {

    public List<UserCoupon> getUserCouponsByUserId(long userId);

    @Insert("INSERT INTO user_coupon (user_id, coupon_id, coupon_end_date) VALUES (#{userId}, #{couponId}, #{couponEndDate})")
    public long insertUserCoupon(UserCoupon userCoupon);

    @Update("UPDATE user_coupon SET state = 1 WHERE user_id = #{userId} and coupon_id = #{couponId}")
    public long updateCouponState(long userId, long couponId);

    @Select("select count(coupon_id) from user_coupon where user_id = #{userId} and coupon_id = 1")
    public Integer selectCouponState(long userId);



}
