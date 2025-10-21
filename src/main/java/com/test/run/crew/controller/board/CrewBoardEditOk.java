package com.test.run.crew.controller.board;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.test.run.crew.model.BoardDAO;
import com.test.run.crew.model.BoardDTO;

/**
 * 크루 게시판의 게시글 수정을 처리하는 서블릿
 * 수정된 게시글 데이터를 DB에 반영한다.
 */
@WebServlet(value = "/crewboardeditok.do")
@MultipartConfig
public class CrewBoardEditOk extends HttpServlet {

	/**
	 * HTTP POST 요청을 처리한다.
	 * 수정된 게시글 제목, 내용, 첨부파일을 받아 DB를 업데이트한다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		String boardContentSeq = req.getParameter("boardContentSeq");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String oldAttach = req.getParameter("oldAttach");

		Part part = req.getPart("attach");
		String saveFileName = "";

		if (part != null && part.getSize() > 0) {
			String uuid = UUID.randomUUID().toString();
			saveFileName = uuid + "_" + getFileName(part);

			String uploadPath = req.getServletContext().getRealPath("/crewboardFile");

			part.write(uploadPath + "/" + saveFileName);

			if (oldAttach != null && !oldAttach.equals("")) {
				File oldFile = new File(uploadPath + "/" + oldAttach);
				if (oldFile.exists()) {
					oldFile.delete();
				}
			}
		} else {
			saveFileName = oldAttach;
		}

		BoardDTO dto = new BoardDTO();
		dto.setBoardContentSeq(Integer.parseInt(boardContentSeq));
		dto.setTitle(title);
		dto.setContent(content);
		dto.setAttach(saveFileName);

		BoardDAO dao = new BoardDAO();
		int result = 0;

		try {
			result = dao.update(dto);
		} catch (Exception e) {
			System.out.println("CrewBoardEditOk.doPost()");
			e.printStackTrace();
		} finally {
			dao.close();
		}

		if (result == 1) {
			resp.sendRedirect("/alldayrun/crewboardview.do?boardContentSeq=" + boardContentSeq);
		} else {
			System.out.println("수정 실패");
		}

	}

	/**
	 * Part 객체에서 파일 이름을 추출한다.
	 * 
	 * @param part 파일 정보를 담고 있는 Part 객체
	 * @return 파일 이름을 반환한다.
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