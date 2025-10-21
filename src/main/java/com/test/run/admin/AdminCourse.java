package com.test.run.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.course.model.CourseDAO;
import com.test.run.course.model.CourseDTO;

/**
 * 관리자 페이지의 코스 관리 서블릿
 * 승인 대기 중인 코스 목록을 조회하여 JSP로 전달한다.
 */
@WebServlet(value = "/admin/admincourse.do")
public class AdminCourse extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리한다.
	 * 승인 대기 중인 코스 목록을 조회하여 admincourse.jsp로 전달
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// AdminCourse.java
		CourseDAO dao = new CourseDAO();

		List<CourseDTO> list = dao.adminGetPendingCourses();

		req.setAttribute("list", list);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/admincourse.jsp");
		dispatcher.forward(req, resp);
	}

}