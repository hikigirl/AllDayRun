package com.test.run.course;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.course.model.CourseDAO;
import com.test.run.course.model.CourseDetailDTO;
import com.test.run.course.model.SpotDTO;

/**
 * 코스 상세 정보 페이지를 처리하는 서블릿
 */
@WebServlet(value = "/course/coursedetail.do")
public class CourseDetail extends HttpServlet {
	/**
	 * HTTP GET 요청을 처리한다.
	 * 요청 파라미터로 받은 코스 번호(courseSeq)에 해당하는 코스의 상세 정보를 조회하여 coursedetail.jsp로 전달한다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		// 1. courseSeq 파라미터를 받음
		String courseSeq = req.getParameter("courseSeq");
		CourseDAO dao = new CourseDAO();

		// 2. 코스의 기본 정보 조회 (별도 DTO 또는 Map 사용)
		CourseDetailDTO course = dao.getCourseDetails(courseSeq); // 상세 정보 조회 메서드 (구현 필요)

		// 3. 해당 코스의 모든 지점 목록을 순서대로 조회
		// List<SpotDTO> spotList = dao.getSpotsByCourseSeq(courseSeq);

		// 4. 조회된 데이터를 jsp로 전달
		req.setAttribute("course", course);
		// req.setAttribute("spotList", spotList);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/course/coursedetail.jsp");
		dispatcher.forward(req, resp);

		dao.close();
	}

}