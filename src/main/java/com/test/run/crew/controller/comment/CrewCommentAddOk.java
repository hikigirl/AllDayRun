package com.test.run.crew.controller.comment;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.run.crew.model.CommentDAO;
import com.test.run.crew.model.CommentDTO;
import com.test.run.crew.model.CrewDAO;

/**
 * 크루 게시판 게시글에 새로운 댓글을 추가하는 서블릿
 * 사용자가 입력한 댓글을 데이터베이스에 저장한다.
 */
@WebServlet(value = "/crewcommentaddok.do")
public class CrewCommentAddOk extends HttpServlet {

	/**
	 * HTTP POST 요청을 처리한다.
	 * 댓글 내용, 게시글 번호, 작성자 ID를 받아 데이터베이스에 새로운 댓글을 추가한다.
	 * 성공 시 해당 게시글 보기 페이지로 리디렉션하고, 실패 시 경고창을 띄운다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession();
		String accountId = (String) session.getAttribute("accountId");

		if (accountId == null) {
			resp.sendRedirect("/alldayrun/user/login.do");
			return;
		}

		CommentDAO dao = new CommentDAO();
		CrewDAO crewdao = new CrewDAO();

		String content = req.getParameter("content");
		String boardcontentSeq = req.getParameter("boardContentSeq");
		String crewSeq = crewdao.getCrewSeq(accountId);

		CommentDTO dto = new CommentDTO();

		dto.setAccountId(accountId);
		dto.setBoardContentSeq(Integer.parseInt(boardcontentSeq));
		dto.setCrewSeq(Integer.parseInt(crewSeq));
		dto.setContent(content);

		int result = dao.add(dto);

		if (result == 1) {
			resp.sendRedirect(
					"/alldayrun/crewboardview.do?boardContentSeq=" + boardcontentSeq);
		} else {
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html; charset=UTF-8");

			PrintWriter writer = resp.getWriter();
			writer.print("<html>");
			writer.print("<body>");
			writer.print("<script>");
			writer.print("alert('댓글 작성에 실패했습니다.');");
			writer.print("history.back();"); // 이전 페이지로 가기
			writer.print("</script>");
			writer.print("</body>");
			writer.print("</html>");
			writer.close();
		}

	}

}
