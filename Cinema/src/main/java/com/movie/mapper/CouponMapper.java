package com.movie.mapper;

import com.movie.domain.Coupons;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CouponMapper {

    @Insert("INSERT INTO coupons (coupon_title, coupon_type, coupon_price) " +
            " VALUES (#{couponTitle}, #{couponType}, #{couponPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long insertCoupon(Coupons coupons);

    @Select("SELECT * FROM coupons ORDER BY id DESC")
    List<Coupons> couponList();

    @Select("SELECT * FROM coupons WHERE id = #{id}")
    Coupons couponDetail(long id);

    @Update("UPDATE coupons SET coupon_title = #{couponTitle}, coupon_type = #{couponType}, coupon_price = #{couponPrice} WHERE id = #{id}")
    long updateCoupon(Coupons coupons);

    @Delete("DELETE FROM coupons WHERE id = #{id}")
    long deleteCoupon(long id);
}
