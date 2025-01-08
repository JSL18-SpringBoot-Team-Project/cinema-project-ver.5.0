package com.movie.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
	private final JavaMailSender mailSender;
	
	@Autowired
	public EmailUtil(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
//	이메일 전송
	public void sendEmail(String toEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		
		mailSender.send(message);
	}
}
