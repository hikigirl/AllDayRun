package com.test.run.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.test.run.course.model.CourseDAO;
import com.test.run.course.model.CourseDTO;

@WebServlet(value = "/course/courseregister.do")
public class CourseRegister extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//CourseRegister.java
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/course/courseregister.jsp");
		dispatcher.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//데이터 수신
		req.setCharacterEncoding("UTF-8");
		
		String lat = req.getParameter("lat");
		String lng = req.getParameter("lng");
		String place = req.getParameter("place");
		
		//db작업
		CourseDAO dao = new CourseDAO();
		CourseDTO dto = new CourseDTO();
		dto.setLat(lat);
		dto.setLng(lng);
		dto.setPlace(place);
		
		int result = dao.addSpot(dto);
		
		// 여기서는 DB 연동을 생략하고, 성공했다고 가정합니다.
		//int result = 1;
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		if(result==1) {
			dataMap.put("result", "success");
		} else {
			dataMap.put("result", "fail");
		}
		
		JSONObject json = new JSONObject(dataMap);
		
		resp.setContentType("application/json"); // 응답 데이터가 JSON임을 명시
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		writer.print(json);
		writer.close();
		
	}
	
}