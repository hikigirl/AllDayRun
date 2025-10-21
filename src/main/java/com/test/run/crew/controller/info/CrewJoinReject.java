package com.test.run.crew.controller.info;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.crew.model.CrewDAO;

@WebServlet(value = "/crewjoinreject.do")
public class CrewJoinReject extends HttpServlet {


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		  String crewJoinSeq = req.getParameter("crewJoinSeq");
	        String crewSeq = req.getParameter("crewSeq");

	        CrewDAO dao = new CrewDAO();
	        try {
	            dao.rejectJoinRequest(crewJoinSeq); // 요청 상태 '거절'로 변경
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            dao.close();
	        }
	        // 새로고침처럼 다시 리스트 페이지로
	        resp.sendRedirect("/alldayrun/crewjoinrequestlist.do?crewSeq=" + crewSeq);

	}

}