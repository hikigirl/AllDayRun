package com.test.run.course;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/course/coursemain.do")
public class CourseMain extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//CourseMain.java
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/course/coursemain.jsp");
		dispatcher.forward(req, resp);
	}
}