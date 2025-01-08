package com.movie.service;

import com.movie.domain.Coupons;
import com.movie.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
