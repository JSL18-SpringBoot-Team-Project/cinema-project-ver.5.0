package com.movie.service;

import com.movie.domain.Coupons;
import com.movie.domain.Events;
import com.movie.domain.MemberCoupon;
import com.movie.domain.UserCoupon;
import com.movie.mapper.CouponMapper;
import com.movie.mapper.EventMapper;
import com.movie.mapper.UserCouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private  EventMapper eventMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    public long insertEvent(Events events) {
        return eventMapper.insertEvent(events);
    }

    public List<Events> eventManageList() {
        return eventMapper.eventManageList();
    }

    public Events eventDetail(long id) {
        return eventMapper.eventDetail(id);
    }

    public long updateEvent(Events events) {
        return eventMapper.updateEvent(events);
    }

    public long deleteEvent(long id) {
        return eventMapper.deleteEvent(id);
    }

    public List<Events> eventStart() {
        return eventMapper.eventStart();  // Mapper 메서드 호출
    }

    public List<Events> eventEnd() {
        return eventMapper.eventEnd();  // Mapper 메서드 호출
    }


    public void userEvent(long user_id) {

        Integer hasCoupon = userCouponMapper.selectCouponState(user_id);
        if(hasCoupon == 0) {
            UserCoupon userCoupon = new UserCoupon();
            System.out.println("user_id = " + user_id);
            userCoupon.setUserId(user_id);
            userCoupon.setCouponId((long) 1);
            userCoupon.setCouponEndDate(LocalDateTime.now().plusDays(7));
            userCouponMapper.insertUserCoupon(userCoupon);
        }
    }
    public long userid(long userid, long couponId){
        return eventMapper.userid(userid, couponId);
    }

    public void usercoupon(long userId, long couponId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // eventMapper.usercoupon 메서드 호출하여 데이터 삽입
        eventMapper.usercoupon(userId, couponId, startDateTime, endDateTime);
    }


}
