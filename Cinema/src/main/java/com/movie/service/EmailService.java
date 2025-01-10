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
		message.setSubject("ムービープロ 認証コードのお知らせ");
		message.setText(
				"こんにちは！\n\n" +
						"ムービープロをご利用いただきありがとうございます。\n" +
						"以下の認証コードを入力して、認証プロセスを完了してください。\n\n" +
						"認証コード: " + code + "\n\n" +
						"このコードは5分間有効です。\n" +
						"\n" +
						"何かご不明な点がございましたら、いつでもお問い合わせください。\n" +
						"\n" +
						"ムービープロチーム\n"
		);
		mailSender.send(message);
	}

	public void sendContactEmail(String userEmail, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(userEmail);
		message.setTo(adminEmail);
		message.setSubject("ムービープロ 新しいお問い合わせ");
		message.setText(
				"こんにちは！\n\n" +
						"以下の内容で新しいお問い合わせを受け付けました。\n" +
						"-----------------------------------\n" +
						"送信者: " + userEmail + "\n" +
						"お問い合わせ内容:\n" + content + "\n" +
						"-----------------------------------\n\n" +
						"出来るだけ早くご対応いたします。\n" +
						"\n" +
						"ムービープロチーム\n"
		);
		mailSender.send(message);
	}
	
	public boolean verifyCode(String email, String code) {
		return code.equals(verificationCodes.get(email));
	}
}
