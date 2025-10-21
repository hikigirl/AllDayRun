package com.test.run.user;

/**
 * 사용자 회원가입 유형 선택 페이지를 처리하는 클래스
 * 일반 회원가입 또는 소셜 로그인(예: Google)을 선택하는 페이지를 제공한다.
 */

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/user/registerselect.do")
public class RegisterSelect extends HttpServlet {

	    /**
	     * GET 요청을 처리한다. 회원가입 유형 선택 페이지를 클라이언트에게 반환
	     * 
	     * @param req 클라이언트로부터의 HttpServletRequest 객체
	     * @param resp 클라이언트로의 HttpServletResponse 객체
	     * @throws ServletException 서블릿 관련 오류
	     * @throws IOException 입출력 오류
	     */
		// RegisterSelect.java

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/registerselect.jsp");
		dispatcher.forward(req, resp);
	}

}