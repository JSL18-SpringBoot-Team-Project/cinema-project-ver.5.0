package com.movie.config;

import com.movie.domain.SessionUser;
import com.movie.domain.User;
import com.movie.mapper.UserMapper;
import com.movie.service.CustomOAuth2UserService;
import com.movie.service.CustomUserDetailsService;
import com.movie.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {

    @Autowired
    private EventService eventService;

    private final UserMapper userMapper;

    public SecurityConfig(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSession session, CustomOAuth2UserService customOAuth2UserService, AuthenticationFailureHandler failureHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/index", "/event/**", "/poster/**", "/user/**", "/css/**", "/images/**", "/js/**", "/fonts/**", "/api/**", "/test/**", "/movie/**", "/booking/**").permitAll()
                        .requestMatchers("/contact").hasRole("USER")
                        .requestMatchers("/mypage/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/")
                        .loginProcessingUrl("/user/login")
                        .defaultSuccessUrl("/", true)
                        .failureHandler(failureHandler)
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler(session))
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(authenticationSuccessHandler(session))
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(HttpSession session) {
        return (request, response, authentication) -> {
            Object principal = authentication.getPrincipal();

            SessionUser sessionUser;
            List<GrantedAuthority> authorities = new ArrayList<>();

            if (principal instanceof OAuth2User oAuth2User) {
                // 소셜 로그인 사용자 처리
                String email = oAuth2User.getAttribute("email");

                User user = userMapper.findByEmail(email)
                        .orElseThrow(() -> new IllegalStateException("소셜 사용자 정보를 찾을 수 없습니다."));

                sessionUser = new SessionUser(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getSocialProvider());
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                eventService.userEvent(sessionUser.getId());

            } else {
                // 일반 로그인 사용자 처리
                String username = authentication.getName();

                User user = userMapper.findByEmail(username)
                        .orElseThrow(() -> new IllegalStateException("일반 사용자 정보를 찾을 수 없습니다."));

                sessionUser = new SessionUser(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getSocialProvider());

                authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
            }

            // 세션에 사용자 정보 저장
            session.setAttribute("USER", sessionUser);
            System.out.println("세션에 저장된 데이터: " + sessionUser);

            // 권한 갱신
            Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    authorities
            );
            SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

            response.sendRedirect("/");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2UserService<org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest, OAuth2User> customOAuth2UserServiceBean() {
        return new CustomOAuth2UserService(userMapper);
    }
}
