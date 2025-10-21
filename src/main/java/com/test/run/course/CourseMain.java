package com.test.run.course;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.run.course.model.CourseCardDTO;
import com.test.run.course.model.CourseDAO;

/**
 * 코스 메인 페이지를 처리하는 서블릿
 * 검색, 인기 코스 조회, 추천 코스 조회 등의 기능을 수행한다.
 */
@WebServlet(value = "/course/coursemain.do")
public class CourseMain extends HttpServlet {
	/**
	 * HTTP GET 요청을 처리한다.
	 * 검색어가 있는 경우 검색 결과를, 없는 경우 로그인 상태에 따라 인기 코스 또는 추천 코스 목록을 조회하여 coursemain.jsp로
	 * 전달한다.
	 * 
	 * @param req  클라이언트의 HttpServletRequest
	 * @param resp 서버의 HttpServletResponse
	 * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
	 * @throws IOException      입출력 예외가 발생할 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// CourseMain.java
		req.setCharacterEncoding("UTF-8");

		CourseDAO dao = new CourseDAO();
		// List<CourseCardDTO> courseList = null;

		// 1. 검색어 확인, 검색 요청인지 일반 페이지 로딩인지 체크
		String keyword = req.getParameter("keyword");

		if (keyword != null && !keyword.trim().isEmpty()) {
			// [검색 시나리오] 검색어가 있는 경우, 검색 기능 수행
			System.out.println("[DEBUG] Searching for keyword: " + keyword);
			List<CourseCardDTO> searchList = dao.searchCourses(keyword); // DB 작업(SELECT)

			// 검색 후에도 입력창에 검색어 남기기 위해 request에 저장
			req.setAttribute("keyword", keyword);
			req.setAttribute("searchList", searchList);
		} else {
			// [일반 페이지 로딩] 검색어가 없는 경우(일반 접근)

			HttpSession session = req.getSession();
			String accountId = (String) session.getAttribute("accountId");
			// 임시-로그인유저 체크되는지 확인용
			// String accountId = "admin@naver.com";

			if (accountId != null) {
				// 로그인 사용자 - 인기 코스 3개 + 지역 기반 추천 코스 3개
				// 1. 인기 코스 3개 조회
				System.out.println("[DEBUG] Logged-in user (" + accountId + "). Getting popular courses.");
				List<CourseCardDTO> popularList = dao.getPopularCourses(3);

				// 2. 지역 기반 추천 코스 3개 조회

				Map<String, String> userLocationMap = dao.getUserLocation(accountId);
				List<CourseCardDTO> recommendedList = null;
				System.out.println("[DEBUG] Logged-in user: userLocationMap" + userLocationMap.get("county") + ", "
						+ userLocationMap.get("district"));

				if (userLocationMap != null && !userLocationMap.isEmpty()) {
					System.out.println("[DEBUG] Logged-in user (" + accountId + "). Getting recommended courses.");
					String county = userLocationMap.get("county");
					String district = userLocationMap.get("district");

					String processedCounty = null;
					if (county != null) {
						processedCounty = county.replace("구", ""); // "강남구" -> "강남"
					}

					String processedDistrict = null;
					if (district != null) {
						processedDistrict = district.replace("동", ""); // "역삼동" -> "역삼"
					}

					System.out.println(
							"[DEBUG] Searching recommendations for: " + processedCounty + ", " + processedDistrict);
					// ✅ [핵심 수정 2] DAO에 손질된 두 개의 키워드를 모두 전달
					recommendedList = dao.getRecommendedCourses(processedCounty, processedDistrict, 3);
				}
				System.out.println("[DEBUG] Logged-in user: popularList" + popularList);
				System.out.println("[DEBUG] Logged-in user: recommendedList" + recommendedList);
				// 3. JSP에 별도의 이름으로 전달

				req.setAttribute("popularList", popularList);
				req.setAttribute("recommendedList", recommendedList);

			} else {
				// 비로그인 사용자: 인기순 코스 조회
				List<CourseCardDTO> popularList = dao.getPopularCourses(6);
				System.out.println("[DEBUG] Guest user. Getting popular courses." + popularList);

				req.setAttribute("popularList", popularList);
			}

		}

		// 3. 결과 전달: 조회된 코스 목록을 JSP에서 사용할 수 있도록 request에 저장
		// req.setAttribute("courseList", courseList);

		// jsp로 다시 넘김
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/course/coursemain.jsp");
		dispatcher.forward(req, resp);

		// 자원정리
		dao.close();
	}
}