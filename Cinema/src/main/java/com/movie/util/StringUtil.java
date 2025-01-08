package com.movie.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
	
//	빈 문자열인지 확인
	public boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
//	문자열 자르기
	public String truncate(String str, int maxLength) {
		if (str == null || str.length() <= maxLength) {
			return str;
		}
		return str.substring(0, maxLength) + "...";
	}
	
//	대소문자 무시 비교
	public boolean equalsIgnoreCase(String str1, String str2) {
		if (str1 == null || str2 == null) return false;
		return str1.equalsIgnoreCase(str2);
	}
}
