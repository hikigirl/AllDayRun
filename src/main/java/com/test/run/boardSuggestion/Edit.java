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

@WebServlet(value = "/boardsuggestion/edit.do")
public class Edit extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Edit.java
		String boardContentSeq = req.getParameter("boardContentSeq");
		
		BoardDAO dao = new BoardDAO();
		
//		BoardDTO dto = dao.get(seq
//							, req.getSession().getAttribute("id").toString());
		BoardDTO dto = dao.get(boardContentSeq);
		
		req.setAttribute("dto", dto);
		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/boardsuggestion/edit.jsp");
		dispatcher.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//EditOk.java 역할
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String boardContentSeq = req.getParameter("boardContentSeq");
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO dto = new BoardDTO();
		dto.setTitle(title);
		dto.setContent(content);
		dto.setBoardContentSeq(boardContentSeq);
		
		if (dao.edit(dto) > 0) {
			resp.sendRedirect("/alldayrun/boardsuggestion/view.do?boardContentSeq=" + boardContentSeq);
		} else {
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}
		
	}

}
