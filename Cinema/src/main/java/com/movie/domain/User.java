package com.movie.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // MyBatis 사용 시 기본 생성자가 필요하므로 @NoArgsConstructor는 거의 필수
@AllArgsConstructor
@Builder
@ToString(exclude = "password") // 보안을 위한 불러올 때 비밀번호 제외
public class User {
    private Integer id;
    private String email;
    private String name;
    private String phone;
    private LocalDate birth;
    private String password;
    private String socialId;
    private SocialProvider socialProvider;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    private List<MemberCoupon> coupons;
    private List<UserCoupon> coupons;
}
