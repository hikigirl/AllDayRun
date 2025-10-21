package com.test.run.running;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/runningmain.do")
public class RunningMain extends HttpServlet {


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//RunningMain.java

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/running/runningmain.jsp");
		dispatcher.forward(req, resp);
	}

}
