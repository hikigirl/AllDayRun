package com.test.run.crew.controller.info;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.crew.model.CrewDAO;

/**
 * 크루 가입 신청 거절 처리를 위한 서블릿
 * 관리자가 가입 신청 목록에서 특정 신청을 거절할 때 호출된다.
 * 
 * @author ehddb
 */
@WebServlet(value = "/crewjoinreject.do")
public class CrewJoinReject extends HttpServlet {

	/**
	 * HTTP POST 요청을 통해 크루 가입 신청을 거절
	 * 요청 파라미터로 받은 'crewJoinSeq'를 사용하여 해당 가입 신청의 상태를 '거절'로 변경
	 * 처리가 완료되면, 해당 크루의 가입 신청 목록 페이지로 다시 리디렉션
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 발생하는 예외
	 * @throws IOException      입출력 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String crewJoinSeq = req.getParameter("crewJoinSeq");
		String crewSeq = req.getParameter("crewSeq");

		CrewDAO dao = new CrewDAO();
		try {
			dao.rejectJoinRequest(crewJoinSeq); // 요청 상태 '거절'로 변경
		} catch (SQLException e) {
			e.printStackTrace(); // 데이터베이스 오류 발생 시 스택 트레이스 출력
		} finally {
			dao.close();
		}
		// 처리가 완료되면 가입 신청 목록 페이지로 리디렉션
		resp.sendRedirect("/alldayrun/crewjoinrequestlist.do?crewSeq=" + crewSeq);

	}

}