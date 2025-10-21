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
 * 크루 등록을 위한 GET 요청을 처리한다.
 * 사용자가 로그인되어 있는지, 그리고 이미 크루의 멤버인지 확인
 * 사용자가 로그인되어 있지 않으면 로그인 페이지로 리다이렉트
 * 사용자가 이미 크루에 소속되어 있으면 알림 메시지 표시
 * 그렇지 않으면 요청은 크루 등록 JSP 페이지로 포워드
 */
@WebServlet(value = "/crewregister.do")
public class CrewRegister extends HttpServlet {

	/**
	 * 크루 등록 페이지에 대한 GET 요청을 처리
	 * 사용자가 로그인되어 있는지 확인한다. 로그인되어 있지 않으면 로그인 페이지로 리다이렉트한다.
	 * 로그인된 사용자가 이미 크루에 연결되어 있는지 확인. 크루에 가입되어있으면 알림을 표시하고 추가 등록을 방지한다.
	 * 크루에 가입하지 않았다면 요청을 크루 등록 JSP로 포워드한다.
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 관련 오류가 발생한 경우
	 * @throws IOException      I/O 오류가 발생한 경우
	 */
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

			if (result) {
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
