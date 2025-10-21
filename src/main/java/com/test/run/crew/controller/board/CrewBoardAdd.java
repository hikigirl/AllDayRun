package com.test.run.crew.controller.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 크루 게시판의 게시글 작성 페이지를 처리하는 서블릿
 */
@WebServlet(value = "/crewboardadd.do")
public class CrewBoardAdd extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리한다.
	 * 게시글 작성 페이지(add.jsp)로 포워딩
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/board/add.jsp");
		dispatcher.forward(req, resp);

	}

}