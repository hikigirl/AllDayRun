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

/**
 * 크루 가입 신청 페이지를 보여주거나 가입 신청을 처리하는 서블릿
 */
@WebServlet(value = "/crewjoin.do")
public class CrewJoin extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리한다.
	 * 크루 가입 신청 폼 페이지로 포워딩한다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// CrewJoin.java

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/join.jsp");
		dispatcher.forward(req, resp);

	}

	/**
	 * HTTP POST 요청을 처리한다.
	 * 특정 크루에 대한 사용자의 가입 신청을 처리한다.
	 * 이미 다른 크루에 속해있는지 확인 후, 가입 신청을 데이터베이스에 추가한다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
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

		if (areYouThere != null) {
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
