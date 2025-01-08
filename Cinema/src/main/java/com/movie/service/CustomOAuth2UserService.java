package com.movie.service;

import com.movie.domain.Role;
import com.movie.domain.SocialProvider;
import com.movie.domain.User;
import com.movie.mapper.UserMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;

    public CustomOAuth2UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (providerId == null || email == null) {
            throw new IllegalArgumentException("Google OAuth2 사용자 정보가 올바르지 않습니다.");
        }

        Optional<User> existingUser = userMapper.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setSocialId(providerId);
            user.setSocialProvider(SocialProvider.GOOGLE);
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateSocialUser(user);
        } else {
            user = User.builder()
                    .email(email)
                    .name(name)
                    .socialId(providerId)
                    .socialProvider(SocialProvider.GOOGLE)
                    .role(Role.ROLE_USER)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userMapper.insertSocialUser(user);
        }

        return oAuth2User;
    }

}
