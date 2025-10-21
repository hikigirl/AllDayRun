package com.test.run.crew.controller.board;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.test.run.crew.model.BoardDAO;
import com.test.run.crew.model.BoardDTO;



@WebServlet(value = "/crewboardeditok.do")
@MultipartConfig
public class CrewBoardEditOk extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String boardContentSeq = req.getParameter("boardContentSeq");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String oldAttach  = req.getParameter("oldAttach");
		
		Part part = req.getPart("attach");
		String saveFileName = "";
		
		if(part != null && part.getSize() > 0	) {
			String uuid = UUID.randomUUID().toString();
			saveFileName = uuid + "_" + getFileName(part);
			
			String uploadPath = "/Users/jeonjaeman/eclipse-workspace/AllDayRun/src/main/webapp/crewboardFile";
			
			part.write(uploadPath + "/" + saveFileName);
			
			if( oldAttach != null && !oldAttach.equals("")) {
			   File oldFile = new File( uploadPath + "/" + oldAttach);
			   if(oldFile.exists()) {
				   oldFile.delete();
			   }
			}
		} else {
			saveFileName = oldAttach;
		}
		
		BoardDTO dto = new BoardDTO();
		dto.setBoardContentSeq(Integer.parseInt(boardContentSeq));
		dto.setTitle(title);
	    dto.setContent(content);
	    dto.setAttach(saveFileName);
		
	   BoardDAO dao = new BoardDAO();
	   int result = 0;
	   
	   try {
		result = dao.update(dto);
	} catch (Exception e) {
		System.out.println("CrewBoardEditOk.doPost()");
		e.printStackTrace();
	} finally {
		dao.close();
	}
	   
	   if( result == 1 ) {
		   resp.sendRedirect("/alldayrun/crewboardview.do?boardContentSeq=" + boardContentSeq);
	   } else {
		   System.out.println("수정 실패");
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