package com.test.run.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
    public static void sendVerificationCode(String smtpUser, String smtpPassword, String toEmail, String code) throws Exception {
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
