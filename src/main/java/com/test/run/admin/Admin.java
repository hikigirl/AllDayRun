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

@WebServlet(value = "/admin/admin.do")
public class Admin extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//admin.java
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
