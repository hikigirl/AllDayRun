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

/**
 * 게시글 삭제를 처리하는 서블릿
 * GET 요청 시 삭제할 게시글 정보를 조회하여 삭제 확인 페이지로 전달하고, POST 요청 시 게시글을 삭제
 */
@WebServlet(value = "/boardsuggestion/del.do")
public class Del extends HttpServlet {

	/**
	 * GET 요청을 처리하여 삭제할 게시글의 정보를 조회하고 삭제 확인 페이지로 포워드
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 예외 발생 시
	 * @throws IOException      입출력 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// Del.java
		String boardContentSeq = req.getParameter("boardContentSeq");
		req.setAttribute("boardContentSeq", boardContentSeq);

		BoardDAO dao = new BoardDAO();

		BoardDTO dto = dao.get(boardContentSeq);
		req.setAttribute("dto", dto);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/boardsuggestion/del.jsp");
		dispatcher.forward(req, resp);
	}

	/**
	 * POST 요청을 처리하여 게시글을 삭제
	 * 삭제 성공 시 게시글 목록 페이지로 리다이렉트하고, 실패 시 에러 메시지를 출력
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 예외 발생 시
	 * @throws IOException      입출력 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// DelOk.java 역할
		String boardContentSeq = req.getParameter("boardContentSeq");

		BoardDAO dao = new BoardDAO();

		if (dao.del(boardContentSeq) > 0) {
			resp.sendRedirect("/alldayrun/boardsuggestion/list.do");
		} else {
			resp.getWriter()
					.print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}

	}

}
