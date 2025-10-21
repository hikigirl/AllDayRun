package com.test.run.boardSuggestion;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.boardSuggestion.model.BoardDAO;
import com.test.run.boardSuggestion.model.BoardDTO;


@WebServlet(value = "/boardsuggestion/del.do")
public class Del extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Del.java
		String boardContentSeq = req.getParameter("boardContentSeq");
		req.setAttribute("boardContentSeq", boardContentSeq);
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO dto = dao.get(boardContentSeq);
		req.setAttribute("dto", dto);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/boardsuggestion/del.jsp");
		dispatcher.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//DelOk.java 역할
		String boardContentSeq = req.getParameter("boardContentSeq");
		
		BoardDAO dao = new BoardDAO();
		
		if (dao.del(boardContentSeq) > 0) {
			resp.sendRedirect("/alldayrun/boardsuggestion/list.do");
		} else {
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}
		
	}
	
	

}
