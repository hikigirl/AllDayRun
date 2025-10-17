package com.test.run.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	
	public void sendVerificationMail(Map<String,String> map) {
		
		//- 보내는 사람(이름,이메일)
		//- 받는 사람(이름,이메일)
		//- 제목
		//- 내용
		
		String username = "보내는 사람 메일 주소";
		String password = "앱 비밀번호(Google)";
		
		//HTTP, Hyper Text Transfer Protocol
		//SMTP, Simple Mail Transfer Protocol
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			
			//메일
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(username));//보내는 사람
			
			message.setRecipients(Message.RecipientType.TO
						, InternetAddress.parse(map.get("email")));
			
			message.setSubject("올데이런에서 발송한 인증 번호입니다.");
			
			String content = """
			
				<h2>인증 번호 발송</h2>
				
				<div style="border: 1px solid #CCC; width: 300px; height: 120px; border-radius: 5px; background-color: #EEE; display: flex; justify-content: center; align-items: center; margin: 20px 0;">
					인증번호: <span style="font-weight: bold;">%s</span>
				</div>
				
				<div>위의 인증 번호를 확인하세요.</div>
							
			""".formatted(map.get("validNumber"));
			
			message.setContent(content, "text/html; charset=UTF-8");
			
			Transport.send(message);
			
			System.out.println("이메일 전송 완료!!");
			
		} catch (Exception e) {
			System.out.println("MailSender.sendMail()");
			e.printStackTrace();
		}
		
	}
	
}







