package com.test.run.crew.controller.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.crew.model.BoardDAO;

@WebServlet(value = "/crewboardlike.do")
public class CrewBoardLike extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String boardContentSeq = req.getParameter("boardContentSeq");
		
		BoardDAO dao = new BoardDAO();
		
		try {
			dao.updateLike(boardContentSeq);
		} catch (Exception e) {
			System.out.println("CrewBoardLike.doGet()");
			e.printStackTrace();
		} finally {
			try {
				dao.close();
			} catch (Exception e) {
				System.out.println("CrewBoardLike.doGet()");
				e.printStackTrace();
			}
		}

	   resp.sendRedirect("/alldayrun/crewboardview.do?boardContentSeq=" +
      boardContentSeq);

	}

}