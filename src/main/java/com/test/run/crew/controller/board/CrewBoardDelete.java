package com.test.run.crew.controller.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.crew.model.BoardDAO;

@WebServlet(value = "/crewboarddelete.do")
public class CrewBoardDelete extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String boardContentSeq = req.getParameter("boardContentSeq");
		int result = 0;
		
		BoardDAO dao = new BoardDAO();
		
		
		try {
			
			result = dao.remove(boardContentSeq);
			
			if(result == 1) {
				resp.sendRedirect("/alldayrun/crewboardlist.do");
			} else {
				System.out.println("삭제를 실패했습니다.");
			} 
			
		} catch (Exception e) {
			System.out.println("CrewBoardDelete.doGet()");
			e.printStackTrace();
		} finally {
			dao.close();
		}
		

	}

}