package com.test.run.crew.controller.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.crew.model.BoardDAO;

/**
 * 크루 게시판의 게시글 좋아요를 처리하는 서블릿
 */
@WebServlet(value = "/crewboardlike.do")
public class CrewBoardLike extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리한다.
	 * 요청 파라미터로 받은 게시글 번호(boardContentSeq)에 해당하는 게시글의 좋아요 수를 1 증가시킨다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
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