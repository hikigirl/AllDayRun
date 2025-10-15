package com.test.run.course;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.run.course.model.CourseCardDTO;
import com.test.run.course.model.CourseDAO;

@WebServlet(value = "/course/coursemain.do")
public class CourseMain extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//CourseMain.java
		req.setCharacterEncoding("UTF-8");
		
		CourseDAO dao = new CourseDAO();
		List<CourseCardDTO> courseList = null;
		
		//1. 검색어 확인, 검색 요청인지 일반 페이지 로딩인지 체크
		String keyword = req.getParameter("keyword");
		
		if (keyword != null && !keyword.trim().isEmpty()) {
			//검색어가 있는 경우, 검색 기능 수행
			System.out.println("[DEBUG] Searching for keyword: " + keyword);
			courseList = dao.searchCourses(keyword); //DB 작업(SELECT)
			
			//검색 후에도 입력창에 검색어 남기기 위해 request에 저장
			req.setAttribute("keyword", keyword);
		} else {
			//검색어가 없는 경우(일반 접근)
			
			HttpSession session = req.getSession();
			String accountId = (String)session.getAttribute("loginUserEmail");
			//임시-로그인유저 체크되는지확인용
			//String accountId = "admin@naver.com";
			
			if (accountId != null) {
				//로그인 사용자 - 지역 기반 추천 코스 조회
				System.out.println("[DEBUG] Logged-in user (" + accountId + "). Getting recommended courses.");
//				String userLocation = dao.getUserLocation(accountId);
//				courseList = dao.getRecommendedCourses(userLocation);
				//임시 - 로그인 사용자도 인기순 코스 조회 일단은
				courseList = dao.getPopularCourses();
			} else {
                // 비로그인 사용자: 인기순 코스 조회
                System.out.println("[DEBUG] Guest user. Getting popular courses.");
                courseList = dao.getPopularCourses();
            }
			
		}
		
		// 3. 결과 전달: 조회된 코스 목록을 JSP에서 사용할 수 있도록 request에 저장
        req.setAttribute("courseList", courseList);
		
        //jsp로 다시 넘김
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/course/coursemain.jsp");
		dispatcher.forward(req, resp);
		
		//자원정리
		dao.close();
	}
}