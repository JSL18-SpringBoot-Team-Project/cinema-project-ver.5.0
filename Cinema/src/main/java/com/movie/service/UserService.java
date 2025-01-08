package com.movie.service;

import com.movie.domain.MemberCoupon;
import com.movie.domain.User;
import com.movie.domain.UserCoupon;
import com.movie.mapper.CouponMapper;
import com.movie.mapper.UserCouponMapper;
import com.movie.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    public void register(User user) {
        if (isEmailRegistered(user.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertUser(user);

        long userid = (long) user.getId();
        System.out.println("1userid = " + userid);
        eventService.userEvent(userid);
    }

    public boolean isEmailRegistered(String email) {
        return userMapper.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public User getUserInfo(long userId) {

        User user = userMapper.getUserInfo(userId);

        List<UserCoupon> coupons = userCouponMapper.getUserCouponsByUserId(userId);

        user.setCoupons(coupons);
        System.out.println("user = " + user);
        return user;
    }

    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    public long deleteUser(long id) {
        return userMapper.deleteUser(id);
    }





}
