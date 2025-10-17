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

@WebServlet(value = "/admin/admincourse.do")
public class AdminCourse extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//AdminCourse.java
		CourseDAO dao = new CourseDAO();
		
		List<CourseDTO> list = dao.adminGetPendingCourses();
		
		req.setAttribute("list", list);
		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/admincourse.jsp");
		dispatcher.forward(req, resp);
	}

}
