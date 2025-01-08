package com.movie.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class DateUtil {
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
//	현재 시간을 기본 형식으로 반환
	public String getCurrentTime() {
		return format(new Date(), DEFAULT_FORMAT);
	}
	
//	특정 날짜 포맷팅
	public String format(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	// LocalDateTime을 포맷팅
	public String format(LocalDateTime dateTime, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return dateTime.format(formatter);
	}

	// 기본 포맷으로 LocalDateTime 포맷팅
	public String format(LocalDateTime dateTime) {
		return format(dateTime, DEFAULT_FORMAT);
	}
	
//	문자열을 Date 객체로 변환
	public Date parse(String dateStr, String format) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(dateStr);
	}
}
