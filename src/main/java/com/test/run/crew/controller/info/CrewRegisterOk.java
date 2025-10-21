package com.test.run.crew.controller.info;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.test.run.crew.model.CrewDAO;
import com.test.run.crew.model.CrewDTO;


@WebServlet(value = "/crewregisterok.do")
@MultipartConfig
public class CrewRegisterOk extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession();
		String accountId = (String) session.getAttribute("accountId");

		// 로그인 안 했을 경우 대비 (필요에 따라 로그인 페이지로 리디렉션)
		if (accountId == null) {
			resp.sendRedirect("/alldayrun/user/login.do");
			return;
		}

		Part part = req.getPart("crewAttach");
		String originFileName = "";
		String savedFileName = "";

		if (part != null && part.getSize() > 0) {

			originFileName = getFileName(part);

			String uuid = UUID.randomUUID().toString();
			savedFileName = uuid + "_" + originFileName;

			String uploadPath = req.getServletContext().getRealPath("/crewmainFile");
			
			part.write(uploadPath + "/" + savedFileName);


		}

		String crewName = req.getParameter("crewName");
		String crewDiscription = req.getParameter("description");
		String regionCity = req.getParameter("regionCity");
		String regionCounty = req.getParameter("regionCounty");
		String regionDistrict = req.getParameter("regionDistrict");
		double latitude = 0;
		double longitude = 0;
		try {
		    latitude = Double.parseDouble(req.getParameter("latitude"));
		    longitude = Double.parseDouble(req.getParameter("longitude"));
		} catch (NumberFormatException e) {
		    System.out.println("위도/경도 값 변환 오류: " + e.getMessage());
		}
		

		CrewDTO dto = new CrewDTO();
		CrewDAO dao = new CrewDAO();
		
	   dto.setCrewName(crewName);
	   dto.setDescription(crewDiscription);
	   dto.setRegionCity(regionCity);
	   dto.setRegionCounty(regionCounty);
	   dto.setRegionDistrict(regionDistrict);
	   dto.setCrewAttach(savedFileName);
	   dto.setLatitude(latitude);
	   dto.setLongitude(longitude);
	   dto.setAccountId(accountId); // 세션에서 가져온 ID 설정
	   
	      System.out.println("DB에 저장할 파일명: " + dto.getCrewAttach()); // 디버깅 코드
	      
	      int result = dao.add(dto);	   
	   if(result == 1 ) {
		   resp.sendRedirect("/alldayrun/crewmain.do");
	   }else {
		   resp.setCharacterEncoding("UTF-8");
	        resp.setContentType("text/html; charset=UTF-8");
	  
	        PrintWriter writer = resp.getWriter();
	        writer.print("<html>");
	        writer.print("<body>");
	        writer.print("<script>");
	        writer.print("alert('크루 등록에 실패하였습니다.');");
	        writer.print("history.back();"); // 이전 페이지로 가기
	        writer.print("</script>");
	        writer.print("</body>");
	        writer.print("</html>");
	        writer.close();
	   }

	}

	private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf("=") + 2, content.length() - 1);
			}
		}
		return "";
	}
}