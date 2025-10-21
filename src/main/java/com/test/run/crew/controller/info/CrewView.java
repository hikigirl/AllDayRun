package com.test.run.crew.controller.info;

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
import com.test.run.crew.model.CrewDAO;
import com.test.run.crew.model.CrewDTO;

/**
 * 특정 크루의 상세 정보를 조회하고 표시하는 서블릿
 * 크루 번호(crewSeq)를 통해 크루 정보와 해당 크루의 활동 사진 목록을 가져와 JSP 페이지로 전달한다.
 */
@WebServlet("/crewview.do")
public class CrewView extends HttpServlet {

	/**
	 * 크루 상세 정보 페이지에 대한 GET 요청을 처리한다.
	 * 요청 파라미터에서 crewSeq를 받아 해당 크루의 상세 정보와 활동 사진 목록을 조회한다.
	 * 조회된 정보를 request 속성에 저장하고 `/WEB-INF/views/crew/view.jsp`로 포워드
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체.
	 * @throws ServletException 서블릿 관련 오류가 발생한 경우
	 * @throws IOException      I/O 오류가 발생한 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String crewSeq = req.getParameter("crewSeq");

		CrewDAO crewDao = new CrewDAO();
		CrewDTO crewDto = crewDao.get(crewSeq);
		crewDao.close();

		// Fetch activity photos
		BoardDAO boardDao = new BoardDAO();
		List<BoardDTO> photoList = boardDao.getBoardPhotosByCrewSeq(crewSeq);
		boardDao.close();

		req.setAttribute("dto", crewDto);
		req.setAttribute("photoList", photoList);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/view.jsp");
		dispatcher.forward(req, resp);

	}

}