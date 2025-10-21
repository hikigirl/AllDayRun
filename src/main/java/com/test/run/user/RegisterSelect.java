package com.test.run.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/user/registerselect.do")
public class RegisterSelect extends HttpServlet {
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//RegisterSelect.java

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/registerselect.jsp");
		dispatcher.forward(req, resp);
	}

}