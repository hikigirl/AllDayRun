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

/**
 * 특정 크루의 가입 신청 목록 조회를 위한 서블릿
 * 크루의 리더만 접근할 수 있으며, 가입 대기 중인 사용자 목록을 화면에 표시한다.
 * 
 * @author ehddb
 */
@WebServlet(value = "/crewjoinrequestlist.do")
public class CrewJoinRequestList extends HttpServlet {

	/**
	 * HTTP GET 요청을 통해 크루 가입 신청 목록을 조회
	 * 세션을 통해 로그인된 사용자인지 확인한다. 비로그인 시 로그인 페이지로 리디렉션
	 * 현재 로그인된 사용자가 해당 크루의 리더인지 권한을 확인
	 * 리더가 아닐 경우, 경고 메시지를 표시하고 크루 메인 페이지로 리디렉션
	 * 리더일 경우, DAO를 통해 가입 신청 목록을 가져와 request 객체에 저장
	 * 최종적으로 가입 신청 목록을 표시하는 JSP 페이지로 포워딩
	 * 
	 * @param req  HttpServletRequest 객체, 세션 정보를 포함하며, 조회 결과를 JSP로 전달하는 데 사용
	 * @param resp HttpServletResponse 객체, 권한이 없거나 비로그인 상태일 때 리디렉션을 위해 사용
	 * @throws ServletException 서블릿 처리 중 발생하는 예외
	 * @throws IOException      입출력 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		String accountId = (String) session.getAttribute("accountId");

		// 1. 로그인 상태 확인
		if (accountId == null) {
			resp.sendRedirect("/alldayrun/user/login.do");
			return;
		}

		CrewDAO dao = new CrewDAO();

		// 2. 크루 리더 권한 확인
		String crewSeq = dao.getCrewSeq(accountId);
		boolean isLeader = dao.isCrewLeader(accountId, crewSeq);

		// 3. 리더가 아닐 경우 접근 제한
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

		// 4. 가입 신청 목록 조회
		List<CrewJoinRequestDTO> requestList = dao.getJoinRequestsByCrewSeq(crewSeq);
		dao.close();

		// 5. JSP에 데이터 전달
		req.setAttribute("requestList", requestList);
		req.setAttribute("crewSeq", crewSeq);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/joinrequestlist.jsp");
		dispatcher.forward(req, resp);

	}

}
