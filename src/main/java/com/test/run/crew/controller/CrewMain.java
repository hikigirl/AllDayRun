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

@WebServlet(value = "/crewmain.do")
public class CrewMain extends HttpServlet {

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