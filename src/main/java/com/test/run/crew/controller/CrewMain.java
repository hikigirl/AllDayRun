package com.test.run.crew.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.crew.model.CrewDAO;
import com.test.run.crew.model.CrewDTO;

/**
 * 크루 메인 페이지 요청을 처리하는 서블릿
 * 전체 크루 목록, 사용자 위치 기반 주변 크루 목록, 인기 크루 목록을 조회하여 JSP 페이지로 전달한다.
 */
@WebServlet(value = "/crewmain.do")
public class CrewMain extends HttpServlet {

	/**
	 * 크루 메인 페이지에 대한 GET 요청을 처리한다.
	 * 요청 파라미터에서 위도(lat)와 경도(lng)를 받아 주변 크루 목록을 조회한다.
	 * 전체 크루 목록과 인기 크루 목록을 조회하여 request 속성에 저장하고
	 * `/WEB-INF/views/crew/main.jsp`로 포워드
	 * 위도/경도 값이 없거나 잘못된 경우 서울시청의 기본값을 사용한다.
	 * 
	 * @param req  HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 관련 오류가 발생한 경우
	 * @throws IOException      I/O 오류가 발생한 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		CrewDAO dao = new CrewDAO();

		System.err.println("Raw lng parameter: " + req.getParameter("lng"));
		System.err.println("--- CrewMain.doGet() end of raw param log ---");

		try {
			List<CrewDTO> list = dao.CrewList();

			String latStr = req.getParameter("lat");
			String lngStr = req.getParameter("lng");

			double lat = 37.566826; // 기본값: 서울시청
			double lng = 126.9786567;

			if (latStr != null && lngStr != null) {
				try {
					lat = Double.parseDouble(latStr);
					lng = Double.parseDouble(lngStr);
				} catch (NumberFormatException e) {
					// parse 실패 시 기본값 사용
				}
			}

			System.out.println("--- DAO 호출 직전, 실제 사용되는 좌표: lat=" + lat + ", lng=" + lng + " ---");
			List<CrewDTO> nearbyCrewList = dao.listNearby(lat, lng);
			List<CrewDTO> popularList = dao.listPopular();

			// 디버깅용: 가져온 리스트의 크기를 콘솔에 출력
			System.out.println("인기 크루 리스트 크기: " + (popularList != null ? popularList.size() : "null"));

			req.setAttribute("popularCrewList", popularList);
			req.setAttribute("nearbyCrewList", nearbyCrewList);
			RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/main.jsp");
			dispatcher.forward(req, resp);
		} finally {
			dao.close();
		}

	}
}