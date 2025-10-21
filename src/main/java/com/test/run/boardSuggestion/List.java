package com.test.run.boardSuggestion;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.test.run.boardSuggestion.model.BoardDAO;
import com.test.run.boardSuggestion.model.BoardDTO;

/**
 * 게시글 목록을 조회하여 반환하는 서블릿입니다.
 * GET 요청 시 모든 게시글 목록을 데이터베이스에서 가져와 JSP 페이지로 전달
 */
@WebServlet(value = "/boardsuggestion/list.do")
public class List extends HttpServlet {

	/**
	 * GET 요청을 처리하여 게시글 목록을 조회하고 JSP 페이지로 포워드
	 * @param req HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 예외 발생 시
	 * @throws IOException 입출력 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		
		//List.java
		BoardDAO dao = new BoardDAO();
		
		java.util.List<BoardDTO> list = dao.list();
		
		req.setAttribute("list", list);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/boardsuggestion/list.jsp");
		dispatcher.forward(req, resp);
	}

}
