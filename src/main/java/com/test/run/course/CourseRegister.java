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
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.test.run.course.model.CourseDAO;
import com.test.run.course.model.CourseDTO;
import com.test.run.course.model.SpotDTO;


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
		//데이터 수신(courseregister.jsp)
		req.setCharacterEncoding("UTF-8"); //항상 맨 꼭대기에
		
		//현재 세션을 가져옴
		HttpSession session = req.getSession();
		System.out.println("[DEBUG] CourseRegister.java req.getSession: " + session);
		
		//세션에 저장된 loginUserEmail이라는 값을 꺼내서 accountId 변수에 저장,
		//getAttribute 실패시 accountId==null
		String accountId = "admin@naver.com"; //[추가] 사용자 ID (임시 하드코딩)
		
		//세션 연동 부분, 밑에 if문까지 주석 풀기
		/*
		String accountId = (String) session.getAttribute("accountId");
		System.out.println("[DEBUG] CourseRegister.java 현재 로그인한 유저: " + accountId);
		
		
		if (accountId == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 unauthorized
			Map<String, Object> datamap = new HashMap<String, Object>();
			datamap.put("result", "fail");
			datamap.put("message", "로그인이 필요합니다.");
			sendJsonResponse(resp, datamap);
			return;
		}
		*/
		
		StringBuilder sb = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            sb.append(line);
        }
        String jsonStr = sb.toString();
        
        // 디버깅: 수신된 원본 JSON 문자열 출력
        System.out.println("[DEBUG] Received JSON String: " + jsonStr);
		
        CourseDAO dao = new CourseDAO(); //dao 객체 안에 db커넥션이 있어서 json 파싱 전에 객체 생성하는 게 좋다네요...
        
		//json-simple을 사용하여 json을 list로 변환
        //List<CourseDTO> spots = new ArrayList<CourseDTO>();
        List<SpotDTO> spots = new ArrayList<SpotDTO>();
        String courseName = "";
        
        
        try {
			JSONParser parser = new JSONParser();
			
			//JSONArray jsonArray = (JSONArray)parser.parse(jsonStr); //json문자열을 jsonArray로 파싱 -> 오류나서 수정
			
			//jsonObject로 파싱
			JSONObject rootObj = (JSONObject)parser.parse(jsonStr);
			System.out.println("[DEBUG] Root JSON Object: " + rootObj.toJSONString()); //파싱된 최상위 객체 콘솔에 출력
			
			// 코스명 추출 (tblCourse에 저장할 데이터)
	        courseName = (String) rootObj.get("courseName"); //코스명 추출
	        System.out.println("[DEBUG] Course Name: " + courseName);
	        
	        // 'spots' 배열 추출
	        JSONArray jsonArray = (JSONArray)rootObj.get("spots");
	        
			//for문을 돌면서 각 요소를 순회, spotDTO에 순서 부여
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObj = (JSONObject)jsonArray.get(i); // 각 요소를 JSONObject로 변환

                // JSONObject에서 값을 추출
                String place = (String)jsonObj.get("place");
                String lat = String.valueOf(jsonObj.get("lat")); // lat, lng는 Double일 수 있으므로 String으로 변환
                String lng = String.valueOf(jsonObj.get("lng"));
				
				// CourseDTO 객체를 만들어 값 설정
				SpotDTO spotDto = new SpotDTO();
				spotDto.setLat(lat);
				spotDto.setLng(lng);
				spotDto.setPlace(place);
				spotDto.setSpotStep(i);
				
				spots.add(spotDto);
			}
			
		} catch (ParseException e) {
			// handle exception			
			System.out.println("CourseRegister.doPost - JSON Parsing Error");
			e.printStackTrace();
			
			// 파싱 오류 발생 시 DAO 호출 없이 실패 응답 (메소드 분리 권장)
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("result", "fail");
            sendJsonResponse(resp, dataMap); // ✅ [수정] 아래의 헬퍼 메소드 사용
            return;
		}
        
		//db작업(CourseDAO)
        // 3. DAO에 코스명, 사용자 ID, 지점 목록을 모두 전달
        // result를 DAO 호출 후에 선언 및 사용하도록 변경
        int result = dao.addCourseTransaction(courseName, accountId, spots); // [수정] DAO 메서드 호출 시그니처 변경
        
        // 4. 결과 응답
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if(result > 0) {
            dataMap.put("result", "success");
        } else {
            dataMap.put("result", "fail");
        }
		
        sendJsonResponse(resp, dataMap); // [수정] sendJsonResponse(헬퍼 메소드) 사용
        dao.close();
	}
	
	private void sendJsonResponse(HttpServletResponse resp, Map<String, Object> dataMap) throws IOException {
        JSONObject json = new JSONObject(dataMap);
        resp.setContentType("application/json"); // 응답 데이터가 JSON임을 명시
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.print(json);
        writer.close();
    }
}