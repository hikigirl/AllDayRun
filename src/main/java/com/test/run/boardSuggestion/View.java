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

@WebServlet(value = "/boardsuggestion/view.do")
public class View extends HttpServlet {

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
