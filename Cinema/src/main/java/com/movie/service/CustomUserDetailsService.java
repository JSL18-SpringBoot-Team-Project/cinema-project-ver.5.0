package com.movie.service;

import com.movie.domain.User;
import com.movie.mapper.UserMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User loadUserInfoByEmail(String email) {
        return userMapper.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Optional로 처리된 결과 확인 및 변환
        User user = userMapper.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // Spring Security의 User 객체 생성
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // 사용자 ID
                user.getPassword(), // 비밀번호
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())) // 권한
        );
    }
}
