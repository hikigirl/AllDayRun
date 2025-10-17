package com.test.run.crew.controller.info;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.run.crew.model.CrewDAO;

@WebServlet(value = "/crewregister.do")
public class CrewRegister extends HttpServlet {

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
			HttpSession session = req.getSession();
			String accountId = (String) session.getAttribute("accountId");

			if (accountId == null) {
				resp.sendRedirect("/alldayrun/user/login.do");
				return;
			}

			CrewDAO dao = new CrewDAO();
	
			try {
				Boolean result = dao.isUserInCrew(accountId);
	
				if(result) {
					resp.setCharacterEncoding("UTF-8");
				    resp.setContentType("text/html; charset=UTF-8");
	
				    PrintWriter writer = resp.getWriter();
				    writer.print("<html><body>");
				    writer.print("<script>");
				    writer.print("alert('이미 소속된 크루가  있습니다. 하나의 계정당 하나의 크루만 가입할 수 있습니다.');");
				    writer.print("history.back();");
				    writer.print("</script>");
				    writer.print("</body></html>");
				    writer.close();
				} else {
	
					RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/register.jsp");
					dispatcher.forward(req, resp);
	
				}
			} finally {
				dao.close();
			}
		}
}
