package com.test.run.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 이메일 관련 유틸리티 기능을 제공하는 클래스
 * 주로 인증 코드 발송과 같은 이메일 전송 기능을 담당한다.
 */
public class EmailUtil {
    /**
     * 지정된 이메일 주소로 인증 코드를 발송
     * Gmail SMTP 서버를 통해 이메일을 전송한다.
     * 
     * @param smtpUser     SMTP 서버 사용자 이름 (ex: Gmail 주소)
     * @param smtpPassword SMTP 서버 비밀번호 (ex: Gmail 앱 비밀번호)
     * @param toEmail      인증 코드를 받을 수신자 이메일 주소
     * @param code         발송할 인증 코드
     * @throws Exception 이메일 전송 중 오류가 발생할 경우
     */
    public static void sendVerificationCode(String smtpUser, String smtpPassword, String toEmail, String code)
            throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUser, "AllDayRun"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("[올데이런] 이메일 인증번호");
        message.setText("인증번호: " + code + "\n10분 안에 입력해주세요.");
        Transport.send(message);
    }
}
