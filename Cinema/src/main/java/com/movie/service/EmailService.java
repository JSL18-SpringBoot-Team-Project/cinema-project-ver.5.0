package com.movie.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	private Map<String, String> verificationCodes = new HashMap<>();

	@Value("${spring.mail.username}")
	private String adminEmail;
	
	public void sendVerificationCode(String email) {
		String code = String.format("%06d", new Random().nextInt(1000000));
		verificationCodes.put(email, code);
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("안녕하세용 인증번홉니다");
		message.setText("인증 ㄱㄱ혓: " + code);
		mailSender.send(message);
	}

	public void sendContactEmail(String userEmail, String adminEmail, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(userEmail);
		message.setTo(adminEmail);
		message.setSubject("문의입니당");
		message.setText(content);
		mailSender.send(message);
	}
	
	public boolean verifyCode(String email, String code) {
		return code.equals(verificationCodes.get(email));
	}
}
