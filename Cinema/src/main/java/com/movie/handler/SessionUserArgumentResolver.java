package com.movie.handler;

import com.movie.domain.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// 컨트롤러 메서드의 파라미터로 바로 세션 정보를 받기
@Component
public class SessionUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession session;

    public SessionUserArgumentResolver(HttpSession session) {
        this.session = session;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 파라미터 타입이 SessionUser일 경우 처리
        return SessionUser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // 세션에서 사용자 정보 반환
        return session.getAttribute("USER");
    }
}
