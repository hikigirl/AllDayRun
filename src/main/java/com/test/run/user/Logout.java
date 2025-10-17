package com.test.run.user;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/user/logout.do")
public class Logout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false); // 기존 세션 가져오기
        if (session != null) {
            session.invalidate(); // 세션 전체 삭제
        }

        // 로그아웃 후 메인 페이지로 이동
        resp.sendRedirect(req.getContextPath() + "/index.do");
    }
}
