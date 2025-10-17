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

@WebServlet("/crewview.do")
public class CrewView extends HttpServlet {

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