package com.test.run.crew.controller.info;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.run.crew.model.CrewDAO;
import com.test.run.crew.model.CrewJoinRequestDTO;

@WebServlet(value = "/crewjoinrequestlist.do")
public class CrewJoinRequestList extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		
		HttpSession session = req.getSession();
	    String accountId = (String) session.getAttribute("accountId");

	    if (accountId == null) {
			resp.sendRedirect("/alldayrun/user/login.do");
			return;
		}

	    CrewDAO dao = new CrewDAO();
	    
	    // 1. Authorization Check: Is the logged-in user the leader of this crew?
	    // This method needs to be implemented in CrewDAO
	    String crewSeq = dao.getCrewSeq(accountId);
	    System.out.println(crewSeq);
	    boolean isLeader = dao.isCrewLeader(accountId, crewSeq);
	    
	    if (!isLeader) {
	        resp.setCharacterEncoding("UTF-8");
	        resp.setContentType("text/html; charset=UTF-8");
	        PrintWriter writer = resp.getWriter();
	        writer.print("<html><body>");
	        writer.print("<script>");
	        writer.print("alert('크루의 리더가 아닙니다!');");
	        writer.print("window.location.href = '/alldayrun/crewmain.do';");
	        writer.print("</script>");
	        writer.print("</body></html>");
	        writer.close();
	        dao.close();
	        return;
	    }

	    // 2. Get the list of join requests for this crew
	    // This method needs to be implemented in CrewDAO
	    List<CrewJoinRequestDTO> requestList = dao.getJoinRequestsByCrewSeq(crewSeq);
	    dao.close();

	    req.setAttribute("requestList", requestList);
	    req.setAttribute("crewSeq", crewSeq);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/joinrequestlist.jsp");
		dispatcher.forward(req, resp);

	}

}
