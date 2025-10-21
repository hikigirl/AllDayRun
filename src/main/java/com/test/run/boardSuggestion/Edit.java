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
 * 게시글 수정을 처리하는 서블릿
 * GET 요청 시 수정할 게시글의 기존 내용을 불러와 수정 페이지를 반환하고, POST 요청 시 수정된 내용을 데이터베이스에 반영
 */
@WebServlet(value = "/boardsuggestion/edit.do")
public class Edit extends HttpServlet {

	/**
	 * GET 요청을 처리하여 수정할 게시글의 기존 정보를 조회하고 수정 페이지로 포워드
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 예외 발생 시
	 * @throws IOException      입출력 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// Edit.java
		String boardContentSeq = req.getParameter("boardContentSeq");

		BoardDAO dao = new BoardDAO();

		// BoardDTO dto = dao.get(seq
		// , req.getSession().getAttribute("id").toString());
		BoardDTO dto = dao.get(boardContentSeq);

		req.setAttribute("dto", dto);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/boardsuggestion/edit.jsp");
		dispatcher.forward(req, resp);
	}

	/**
	 * POST 요청을 처리하여 수정된 게시글 내용을 데이터베이스에 반영
	 * 수정 성공 시 해당 게시글의 상세 보기 페이지로 리다이렉트하고, 실패 시 에러 메시지를 출력
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 예외 발생 시
	 * @throws IOException      입출력 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// EditOk.java 역할
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
			resp.getWriter()
					.print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}

	}

}
