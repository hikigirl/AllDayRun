package com.test.run.crew.controller.board;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.test.run.crew.model.BoardDAO;
import com.test.run.crew.model.BoardDTO;
import com.test.run.crew.model.CrewDAO;

/**
 * 크루 게시판의 게시글 작성을 처리하는 서블릿
 * 작성된 게시글 데이터를 DB에 저장한다.
 */
@WebServlet(value = "/crewboardaddok.do")
@MultipartConfig
public class CrewBoardAddOk extends HttpServlet {

	/**
	 * HTTP POST 요청을 처리한다.
	 * 게시글 제목, 내용, 첨부파일을 받아 DB에 저장한다.
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

		String title = req.getParameter("title");
		String content = req.getParameter("content");

		Part part = req.getPart("attach");
		String originFileName = "";
		String savedFileName = "";

		if (part != null && part.getSize() > 0) {

			originFileName = getFileName(part);

			String uuid = UUID.randomUUID().toString();
			savedFileName = uuid + "_" + originFileName;

			String uploadPath = req.getServletContext().getRealPath("/crewboardFile");

			part.write(uploadPath + "/" + savedFileName);

		}

		CrewDAO crewdao = new CrewDAO();
		BoardDAO dao = new BoardDAO();
		BoardDTO dto = new BoardDTO();

		dto.setTitle(title);
		dto.setContent(content);
		dto.setAttach(savedFileName);

		String crewSeq = crewdao.getCrewSeq(accountId);

		// 임시
		dto.setCrewSeq(Integer.parseInt(crewSeq));
		dto.setAccountId(accountId);
		dto.setBoardContentTypeSeq(1); // 1 일반 or 2 비밀
		// 위에 3개는 로그인 하면 세션에서 가져올 예정

		int result = dao.add(dto);

		if (result == 1) {

			resp.sendRedirect("/alldayrun/crewboardlist.do");

		} else {
			System.out.println("글쓰기 실패");

		}

	}

	/**
	 * Part 객체에서 파일 이름을 추출한다.
	 * 
	 * @param part 파일 정보를 담고 있는 Part 객체
	 * @return 파일 이름을 반환
	 */
	private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf("=") + 2, content.length() - 1);
			}
		}
		return "";
	}

}