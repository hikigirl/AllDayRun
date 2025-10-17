package com.test.run.crew.controller.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.crew.model.BoardDAO;
import com.test.run.crew.model.BoardDTO;

@WebServlet(value = "/crewboardedit.do") // @WebServlet 경로 수정
public class CrewBoardEdit extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. 수정할 게시글 번호 받기
        String boardContentSeq = req.getParameter("boardContentSeq");

        // 2. DAO 위임하여 게시글 데이터 가져오기
        BoardDAO dao = new BoardDAO();
        BoardDTO dto = null;
        
        try {
            dto = dao.get(boardContentSeq);
        } finally {
            dao.close(); // get() 메소드 실행 후 연결 종료
        }

        // 3. JSP에 데이터 전달
        req.setAttribute("dto", dto);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/crew/board/edit.jsp");
        dispatcher.forward(req, resp);
    }
}
