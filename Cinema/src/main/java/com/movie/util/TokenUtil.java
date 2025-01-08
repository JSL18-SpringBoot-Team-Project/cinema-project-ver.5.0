package com.movie.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenUtil {

//	CSRF 토큰 생성
	public String generateToken() {
		return UUID.randomUUID().toString();
	}
	
//	토큰 검증
	public boolean validateToken(String token, String storedToken) {
		return token != null && token.equals(storedToken);
	}
}
