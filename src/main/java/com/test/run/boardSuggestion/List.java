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

@WebServlet(value = "/boardsuggestion/list.do")
public class List extends HttpServlet {

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
