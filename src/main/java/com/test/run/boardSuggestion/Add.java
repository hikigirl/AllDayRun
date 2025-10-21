package com.test.run.boardSuggestion;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.test.run.boardSuggestion.model.BoardDAO;
import com.test.run.boardSuggestion.model.BoardDTO;

@WebServlet(value = "/boardsuggestion/add.do")
public class Add extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Add.java

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/boardsuggestion/add.jsp");
		dispatcher.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//AddOk.java역할
		req.setCharacterEncoding("UTF-8");
		
		//req > multi
		MultipartRequest multi = new MultipartRequest(
								req,
								req.getServletContext().getRealPath("asset/place"),
								1024 * 1024 * 30,
								"UTF-8",
								new DefaultFileRenamePolicy()
				);
		
		System.out.println(req.getServletContext().getRealPath("/asset/place"));
		
		String title = multi.getParameter("title");
		String content = multi.getParameter("content");
//		String attach = multi.getParameter("attach");
		String attach = "attach";
		String accountId = "admin@naver.com";
		int BoardContentTypeSeq = 1;
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO dto = new BoardDTO();
		
		dto.setTitle(title);
		dto.setContent(content);
		dto.setAttach(attach);
		dto.setBoardContentTypeSeq(BoardContentTypeSeq);
		dto.setAccountId(accountId);

		
//		HttpSession session = req.getSession();
//		dto.setAccountId(session.getAttribute("id").toString());
		
		int result = dao.add(dto);
		
		if (result > 0) {
			resp.sendRedirect("/alldayrun/boardsuggestion/list.do");
		} else {
			resp.getWriter()
				.print("<html><meta charset='UTF-8'><script>alert('fail');history.back();</script></html>");
			resp.getWriter().close();
		}
		
	}
	
	

}



