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

@WebServlet(value = "/crewjoin.do")
public class CrewJoin extends HttpServlet {


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//CrewJoin.java

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/join.jsp");
		dispatcher.forward(req, resp);

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String crewSeq = req.getParameter("crewSeq");
	    
	    HttpSession session = req.getSession();
	    String accountId = (String) session.getAttribute("accountId");

	    if (accountId == null) {
			resp.sendRedirect("/alldayrun/user/login.do");
			return;
		}
	    
	    CrewDAO dao = new CrewDAO();
	    
	    String areYouThere = dao.getCrewSeq(accountId);
	    
	    if( areYouThere != null ) {
	    	resp.setCharacterEncoding("UTF-8");
	        resp.setContentType("text/html; charset=UTF-8");
	        PrintWriter writer = resp.getWriter();
	        writer.print("<html><body>");
	        writer.print("<script>");
	        writer.print("alert('이미 크루에 가입되어있습니다!');");
	        writer.print("window.location.href = '/alldayrun/crewmain.do';");
	        writer.print("</script>");
	        writer.print("</body></html>");
	        writer.close();
	    } else {
	    	
	    	int result = dao.addCrewJoinRequest(crewSeq, accountId);
		    dao.close();

		    if (result == 1) {
		        // Success: Redirect to a success page or the crew's main page
		        resp.sendRedirect("/alldayrun/crewmain.do"); // Example: Redirect to crew view page
		    } else {
		        // Failure: Redirect to an error page or show a message
		        resp.sendRedirect("/error.do"); // Example: Redirect to a generic error page
		    }
	    }
	    
	    
	}

}
