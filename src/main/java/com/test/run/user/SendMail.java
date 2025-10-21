package com.test.run.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * 이메일 인증 번호 발송 요청을 처리하는 서블릿
 * 클라이언트로부터 이메일 주소를 받아 5자리 인증 번호를 생성하고,
 * 이를 세션에 저장한 후 {@link MailSender}를 통해 이메일로 발송한다.
 * 발송 결과는 JSON 형태로 클라이언트에 응답합니다.
 */
@WebServlet(value = "/user/sendmail.do")
public class SendMail extends HttpServlet {

	/**
	 * 이메일 인증 번호 발송을 위한 POST 요청을 처리
	 * 1. 요청 파라미터에서 이메일 주소를 가져온다.
	 * 2. 5자리 랜덤 인증 번호를 생성하고 세션에 저장한다.
	 * 3. {@link MailSender}를 사용하여 인증 번호 이메일을 발송한다.
	 * 4. 이메일 발송 성공 여부를 JSON 형태로 응답한다.
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 관련 오류가 발생한 경우
	 * @throws IOException      I/O 오류가 발생한 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// SendMail.java
		// 1. 데이터 가져오기(email)
		// 2. 인증번호 생성 > 세션
		// 3. 이메일 발송
		// 4. 마무리

		String email = req.getParameter("email");

		// 인증번호: 5자리 > 0 ~ 89999 + 10000 > 10000 ~ 99999
		Random rnd = new Random();
		int validNumber = rnd.nextInt(90000) + 10000;

		req.getSession().setAttribute("validNumber", validNumber);

		int result = 0;

		try {

			MailSender sender = new MailSender();

			Map<String, String> map = new HashMap<String, String>();

			map.put("email", email);
			map.put("validNumber", validNumber + "");

			sender.sendVerificationMail(map);

			result = 1;

		} catch (Exception e) {
			System.out.println("SendMail.doGet()");
			e.printStackTrace();
		}

		resp.setContentType("application/json");

		JSONObject obj = new JSONObject();
		obj.put("result", result);

		resp.getWriter().print(obj.toString());
		resp.getWriter().close();

	}

}
