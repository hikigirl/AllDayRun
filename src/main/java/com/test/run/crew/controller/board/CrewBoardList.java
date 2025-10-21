package com.test.run.crew.controller.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.test.run.crew.model.BoardDAO;
import com.test.run.crew.model.BoardDTO;
import com.test.run.crew.model.CrewDAO;

@WebServlet(value = "/crewboardlist.do")
public class CrewBoardList extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		String accountId = (String) session.getAttribute("accountId");

		// 로그인 안 했을 경우 대비
		if (accountId == null) {
			resp.sendRedirect("/alldayrun/user/login.do");
			return;
		}

		CrewDAO crewDao = new CrewDAO();
		String crewSeq = crewDao.getCrewSeq(accountId);
		crewDao.close();

		BoardDAO boardDao = new BoardDAO();
		
		try {
			List<BoardDTO> list = new ArrayList<>();
			String pagebar = "";
			int nowPage = 1;

			if (crewSeq != null) { // 사용자가 크루에 속해 있는 경우에만 게시글을 가져옴
				// --- 페이지네이션 로직 ---
				String page = req.getParameter("page");
				if (page != null && !page.equals("")) {
					nowPage = Integer.parseInt(page);
				}

				int pageSize = 15; // 한 페이지에 보여줄 게시물 수
				int begin = ((nowPage - 1) * pageSize) + 1;
				int end = begin + pageSize - 1;

				HashMap<String, String> map = new HashMap<>();
				map.put("begin", begin + "");
				map.put("end", end + "");

				list = boardDao.list(map, crewSeq); // 해당 페이지의 글 목록

				// --- 페이지바 만들기 (수정된 로직) ---
				int totalCount = boardDao.getTotalCount(crewSeq); // 총 게시물 수
				int totalPage = (int) Math.ceil((double) totalCount / pageSize); // 총 페이지 수

				StringBuilder sbPagebar = new StringBuilder();
				int blockSize = 10; // 한번에 보여줄 페이지 번호 개수
				int n = ((nowPage - 1) / blockSize) * blockSize + 1; // 현재 블록의 시작 페이지 번호
				int loop = 1; // 루프 변수

				sbPagebar.append("<nav><ul class='pagination'>");

				// 이전 10페이지
				if (n == 1) {
					sbPagebar.append(String.format("<li class='disabled'><a href='#!'>&laquo;</a></li>"));
				} else {
					sbPagebar.append(String.format("<li><a href='/alldayrun/crewboardlist.do?page=%d'>&laquo;</a></li>", n - 1));
				}

				while (!(loop > blockSize || n > totalPage)) {
					if (n == nowPage) {
						sbPagebar.append(String.format("<li class='active'><a href='#!'>%d</a></li>", n));
					} else {
						sbPagebar.append(String.format("<li><a href='/alldayrun/crewboardlist.do?page=%d'>%d</a></li>", n, n));
					}
					loop++;
					n++;
				}

				// 다음 10페이지
				if (n > totalPage) {
					sbPagebar.append(String.format("<li class='disabled'><a href='#!'>&raquo;</a></li>"));
				} else {
					sbPagebar.append(String.format("<li><a href='/alldayrun/crewboardlist.do?page=%d'>&raquo;</a></li>", n));
				}

				sbPagebar.append("</ul></nav>");
				pagebar = sbPagebar.toString();
			}
			else { // 사용자가 크루에 속해있지 않은 경우
				resp.setCharacterEncoding("UTF-8");
				resp.setContentType("text/html; charset=UTF-8");
				PrintWriter writer = resp.getWriter();
				writer.print("<html><body>");
				writer.print("<script>");
				writer.print("alert('크루에 가입해주세요!');");
				writer.print("window.location.href = '/alldayrun/crewmain.do';");
				writer.print("</script>");
				writer.print("</body></html>");
				writer.close();
				return; // 리디렉션 후 메소드 종료
			}
			// ---------------------------------------

			req.setAttribute("list", list);
			req.setAttribute("pagebar", pagebar);
			req.setAttribute("nowPage", nowPage);

			RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/board/list.jsp");
			dispatcher.forward(req, resp);

		} finally {
			boardDao.close(); // 서블릿에서 모든 DB 작업이 끝난 후 연결 종료
		}
	}
}
