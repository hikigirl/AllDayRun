package com.test.run.crew.controller.comment;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.run.crew.model.CommentDAO;
import com.test.run.crew.model.CommentDTO;
import com.test.run.crew.model.CrewDAO;

@WebServlet(value = "/crewcommentaddok.do")
public class CrewCommentAddOk extends HttpServlet {


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession();
		String accountId = (String) session.getAttribute("accountId");

		if (accountId == null) {
			resp.sendRedirect("/alldayrun/user/login.do");
			return;
		}

		CommentDAO dao = new CommentDAO();
		CrewDAO crewdao = new CrewDAO();
		
		
		String content = req.getParameter("content");
		String boardcontentSeq  = req.getParameter("boardContentSeq");
		String crewSeq = crewdao.getCrewSeq(accountId);
		
	   
		
		CommentDTO dto = new CommentDTO();
	
		dto.setAccountId(accountId);
		dto.setBoardContentSeq(Integer.parseInt(boardcontentSeq));
		dto.setCrewSeq(Integer.parseInt(crewSeq));
		dto.setContent(content);
       
		int result = dao.add(dto);
		
		if(result == 1 )	{
			 resp.sendRedirect(
				      "/alldayrun/crewboardview.do?boardContentSeq=" + boardcontentSeq);
		}else {
			resp.setCharacterEncoding("UTF-8");
			        resp.setContentType("text/html; charset=UTF-8");
			  
			        PrintWriter writer = resp.getWriter();
			        writer.print("<html>");
			        writer.print("<body>");
			        writer.print("<script>");
			        writer.print("alert('댓글 작성에 실패했습니다.');");
			        writer.print("history.back();"); // 이전 페이지로 가기
			        writer.print("</script>");
			        writer.print("</body>");
			        writer.print("</html>");
			        writer.close();
		}
		
	}

}
