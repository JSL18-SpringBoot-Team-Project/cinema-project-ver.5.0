package com.movie.service;

import com.movie.domain.Coupons;
import com.movie.mapper.CouponMapper;
import com.movie.mapper.UserCouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponMapper couponMapper;

    public long insertCoupon(Coupons coupons) {
        return couponMapper.insertCoupon(coupons);
    }

    public List<Coupons> couponList() {
        return couponMapper.couponList();
    }

    public Coupons couponDetail(long id) {
        return couponMapper.couponDetail(id);
    }

    public long updateCoupon(Coupons coupons) {
        return couponMapper.updateCoupon(coupons);
    }

    public long deleteCoupon(long id) {
        return couponMapper.deleteCoupon(id);
    }

    public boolean isCouponCodeValid(long couponCode) {
        return couponMapper.checkCouponCodeValid(couponCode) != null;
    }

    public void registerCoupon(long userId, long couponCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("couponId", couponCode);
        couponMapper.registerCoupon(params);
    }

}
