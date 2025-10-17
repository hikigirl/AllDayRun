package com.test.run.crew.controller.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.crew.model.BoardDAO;
import com.test.run.crew.model.BoardDTO;
import com.test.run.crew.model.CommentDAO;
import com.test.run.crew.model.CommentDTO;

@WebServlet(value = "/crewboardview.do")
public class CrewBoardViews extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 1. 게시글 번호 받기
	    String boardContentSeq = req.getParameter("boardContentSeq");

	    // 2. DAO 위임
	    BoardDAO dao = new BoardDAO();

	    // 3. 조회수 증가
	    dao.updateReadCount(boardContentSeq);

	    // 4. 게시글 정보 가져오기
	    BoardDTO dto = dao.get(boardContentSeq);
	    
	    // 5. JSP에게 데이터 전달
	    req.setAttribute("dto", dto);
	    
	    CommentDAO commentDAO = new CommentDAO();
	    
	   List<CommentDTO> list = commentDAO.commentList(boardContentSeq);
	   
	   req.setAttribute("commentList", list);
	   

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/board/view.jsp");
		dispatcher.forward(req, resp);
		

	}

}
