package com.test.run.user;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * 사용자 로그아웃 요청을 처리하는 서블릿
 * 현재 세션을 무효화하고 사용자를 메인 페이지로 리다이렉트하여 로그아웃을 완료함
 */
@WebServlet("/user/logout.do")
public class Logout extends HttpServlet {
    /**
     * 로그아웃 요청을 처리
     * 현재 HTTP 세션을 무효화하고, 사용자를 애플리케이션의 메인 페이지로 리다이렉트
     * @param req HttpServletRequest 객체.
     * @param resp HttpServletResponse 객체.
     * @throws ServletException 서블릿 관련 오류가 발생한 경우.
     * @throws IOException I/O 오류가 발생한 경우.
     */
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
