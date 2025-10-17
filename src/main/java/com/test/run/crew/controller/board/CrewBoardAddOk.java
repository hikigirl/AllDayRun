package com.test.run.crew.controller.board;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.test.run.crew.model.BoardDAO;
import com.test.run.crew.model.BoardDTO;
import com.test.run.crew.model.CrewDAO;

@WebServlet(value = "/crewboardaddok.do")
@MultipartConfig
public class CrewBoardAddOk extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession();
		String accountId = (String) session.getAttribute("accountId");

		if (accountId == null) {
			resp.sendRedirect("/alldayrun/user/login.do");
			return;
		}

		String title = req.getParameter("title");
		String content = req.getParameter("content");
		
		Part part = req.getPart("attach");
		String originFileName =  "";
		String savedFileName = "";
		
		if( part != null  && part.getSize() > 0 ) {
			
			originFileName  = getFileName(part);
			
			String uuid = UUID.randomUUID().toString();
			savedFileName = uuid + "_" + originFileName;
			
			String uploadPath = req.getServletContext().getRealPath("/crewboardFile");
			
		
			part.write(uploadPath + "/" + savedFileName);
			
		}
		
		CrewDAO crewdao = new CrewDAO();
		BoardDAO dao = new BoardDAO();
		BoardDTO dto = new BoardDTO();
		
		dto.setTitle(title);
		dto.setContent(content);
		dto.setAttach(savedFileName);
		
		String crewSeq = crewdao.getCrewSeq(accountId);
		
		//임시
		dto.setCrewSeq(Integer.parseInt(crewSeq)); 
		dto.setAccountId(accountId);
		dto.setBoardContentTypeSeq(1); //1 일반  or 2 비밀 
		//위에 3개는 로그인 하면 세션에서 가져올 예정 
		
		int result = dao.add(dto);
		
		if(result == 1 ) {
			
			resp.sendRedirect("/alldayrun/crewboardlist.do");
			
		} else {
			System.out.println("글쓰기 실패");
			
		}
		

	}

	private String getFileName(Part part) {
			for(String content : part.getHeader("content-disposition").split(";")) {
				if(content.trim().startsWith("filename")) {
					return content.substring(content.indexOf("=")+2, content.length() -1 );
				}
			}
		return "";
	}
	
	

}