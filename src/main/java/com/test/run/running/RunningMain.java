package com.test.run.running;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 러닝 메인 페이지 요청을 처리하는 서블릿
 * 요청을 받아 러닝 메인 JSP 페이지로 포워드한다.
 */
@WebServlet(value = "/runningmain.do")
public class RunningMain extends HttpServlet {

	/**
	 * 러닝 메인 페이지에 대한 GET 요청을 처리
	 * 요청을 `/WEB-INF/views/running/runningmain.jsp`로 포워드
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 관련 오류가 발생한 경우
	 * @throws IOException      I/O 오류가 발생한 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// RunningMain.java

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/running/runningmain.jsp");
		dispatcher.forward(req, resp);
	}

}
