package com.test.run.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
		
//		String lat = req.getParameter("lat");
//		String lng = req.getParameter("lng");
//		String place = req.getParameter("place");
		StringBuilder sb = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            sb.append(line);
        }
        String jsonStr = sb.toString();
		
		//json-simple을 사용하여 json을 list로 변환
        List<CourseDTO> spots = new ArrayList<CourseDTO>();
        try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(jsonStr); //문자열을 jsonArray로 파싱
			
			//for문을 돌면서 각 요소를 순회
			for (Object obj : jsonArray) {
				JSONObject jsonObj = (JSONObject) obj; // 각 요소를 JSONObject로 변환

                // JSONObject에서 값을 추출
                String place = (String) jsonObj.get("place");
                String lat = String.valueOf(jsonObj.get("lat")); // lat, lng는 Double일 수 있으므로 String으로 변환
                String lng = String.valueOf(jsonObj.get("lng"));
				
				// CourseDTO 객체를 만들어 값 설정
				CourseDTO dto = new CourseDTO();
				dto.setLat(lat);
				dto.setLng(lng);
				dto.setPlace(place);
				
				spots.add(dto);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("CourseRegister.doPost");
			e.printStackTrace();
		}
        
		//db작업
		CourseDAO dao = new CourseDAO();
		
		
		int result = dao.addCourse(spots);
		
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