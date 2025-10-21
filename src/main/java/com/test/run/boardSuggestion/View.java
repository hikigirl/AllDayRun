package com.test.run.boardSuggestion;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.run.boardSuggestion.model.BoardDAO;
import com.test.run.boardSuggestion.model.BoardDTO;


/**
 * 게시글 상세보기를 처리하는 서블릿입니다.
 * GET 요청 시 특정 게시글의 내용을 데이터베이스에서 조회하여 JSP 페이지로 전달합니다.
 */
@WebServlet(value = "/boardsuggestion/view.do")
public class View extends HttpServlet {

	/**
	 * GET 요청을 처리하여 특정 게시글의 상세 정보를 조회하고 JSP 페이지로 포워드합니다.
	 * @param req HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 예외 발생 시
	 * @throws IOException 입출력 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
//		HttpSession session = req.getSession();
		
		//View.java
		
		// 1. 글 번호(seq) 가져오기
		String boardContentSeq = req.getParameter("boardContentSeq");

		// 2. DAO 호출
		BoardDAO dao = new BoardDAO();
		
//		String accountId = session.getAttribute("accountId");
//		BoardDTO dto = dao.get(boardContentSeq, accountId);

		BoardDTO dto = dao.get(boardContentSeq);

		// 3. 조회수 증가
//		dao.increaseReadCount(boardContentSeq);

		// 4. 데이터 전달
		req.setAttribute("dto", dto);

		// 5. JSP 포워딩
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/boardsuggestion/view.jsp");
		dispatcher.forward(req, resp);
	}

}
