package com.test.run.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.run.course.model.CourseCardDTO;
import com.test.run.course.model.CourseDAO;
import com.test.run.course.model.CourseDTO;

/**
 * 관리자 페이지의 메인 서블릿
 * 관리자 페이지에 필요한 데이터를 조회하고, JSP로 전달한다.
 */
@WebServlet(value = "/admin/admin.do")
public class Admin extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리한다.
	 * 승인 대기 중인 코스 수와 인기 코스 목록을 조회하여 admin.jsp로 전달한다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// admin.java
		CourseDAO dao = new CourseDAO();
		int pendingCount = dao.getPendingCount();

		List<CourseCardDTO> popularList = dao.getPopularCourses(3);

		req.setAttribute("popularList", popularList);

		// JSP로 전달
		req.setAttribute("pendingCount", pendingCount);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/admin.jsp");
		dispatcher.forward(req, resp);
	}

}