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
 * 크루 가입 신청을 승인하는 서블릿
 * 크루장이 가입 신청을 승인하면 해당 사용자를 크루 멤버로 추가한다.
 */
@WebServlet(value = "/crewjoinapprove.do")
public class CrewJoinApprove extends HttpServlet {

	/**
	 * HTTP POST 요청을 처리한다.
	 * 가입 신청 번호를 받아 해당 신청을 승인 처리하고, 사용자를 크루 멤버 테이블에 추가한다.
	 * 처리 후 가입 신청 목록 페이지로 리디렉션한다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String crewJoinSeq = req.getParameter("crewJoinSeq");
		String crewSeq = req.getParameter("crewSeq");

		CrewDAO dao = new CrewDAO();
		try {
			dao.approveJoinRequest(crewJoinSeq); // 승인 + tblCrewMember 등록
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		// 새로고침처럼 다시 리스트 페이지로
		resp.sendRedirect("/alldayrun/crewjoinrequestlist.do?crewSeq=" + crewSeq);

	}

}