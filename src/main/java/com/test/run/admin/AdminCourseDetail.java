package com.test.run.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.course.model.CourseDAO;
import com.test.run.course.model.CourseDetailDTO;

@WebServlet(value = "/admin/admincoursedetail.do")
public class AdminCourseDetail extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//AdminCourseDetail.java
		req.setCharacterEncoding("UTF-8");
		//1. courseSeq 파라미터를 받음
		String courseSeq = req.getParameter("courseSeq");
		CourseDAO dao = new CourseDAO();
		
		//2. 코스의 기본 정보 조회 (별도 DTO 또는 Map 사용)
        CourseDetailDTO course = dao.getCourseDetails(courseSeq); // 상세 정보 조회 메서드 (구현 필요)
		
        //3. 해당 코스의 모든 지점 목록을 순서대로 조회
        //List<SpotDTO> spotList = dao.getSpotsByCourseSeq(courseSeq);
        
        //4. 조회된 데이터를 jsp로 전달
        req.setAttribute("course", course);
        //req.setAttribute("spotList", spotList);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/admincoursedetail.jsp");
		dispatcher.forward(req, resp);
		
		dao.close();
	}
}