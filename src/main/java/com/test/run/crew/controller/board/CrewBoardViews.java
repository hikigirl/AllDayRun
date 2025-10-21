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

/**
 * 크루 게시판의 특정 게시글을 조회하는 서블릿
 * 게시글 조회수 증가, 게시글 내용 및 댓글 목록 조회를 처리한다.
 */
@WebServlet(value = "/crewboardview.do")
public class CrewBoardViews extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리한다.
	 * 게시글 번호를 받아 해당 게시글의 조회수를 증가시키고,
	 * 게시글 데이터와 댓글 목록을 조회하여 view.jsp로 전달한다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
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
