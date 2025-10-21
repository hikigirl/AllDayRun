package com.test.run.user;

import javax.servlet.*;
import javax.servlet.http.*;

import com.test.run.user.model.AccountDAO;
import com.test.run.user.model.AccountDTO;

import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * 사용자 로그인 요청을 처리하는 서블릿
 * GET 요청 시 로그인 페이지를 표시
 * POST 요청 시 사용자 인증을 수행하여 로그인 처리 및 역할에 따른 페이지 리다이렉션을 담당
 */
@WebServlet("/user/login.do")
public class Login extends HttpServlet {
    /**
     * 로그인 페이지를 표시하기 위한 GET 요청을 처리
     * 요청을 `/WEB-INF/views/user/login.jsp`로 포워드
     * 
     * @param req  HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 관련 오류가 발생한 경우
     * @throws IOException      I/O 오류가 발생한 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/login.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * 사용자 로그인 인증을 처리하기 위한 POST 요청을 처리
     * 입력된 아이디와 비밀번호를 검증하고, 유효한 경우 사용자 정보를 세션에 저장한 후
     * 사용자의 역할(관리자/일반)에 따라 다른 페이지로 리다이렉트
     * 인증에 실패하면 로그인 페이지에 에러 메시지를 표시
     * 
     * @param req  HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 관련 오류가 발생한 경우
     * @throws IOException      I/O 오류가 발생한 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String accountId = req.getParameter("accountId");
        String password = req.getParameter("password");

        AccountDAO dao = new AccountDAO();
        boolean ok = dao.validateLogin(accountId, password);

        if (ok) {
            // 계정 정보 가져오기
            AccountDTO dto = dao.findAccount(accountId);
            String role = dto.getAccountRole();

            HttpSession session = req.getSession();

            System.out.println("Login.java: 로그인 성공. accountId = " + accountId + ", role = " + role);
            System.out.println("Login.java: Session ID = " + session.getId());

            session.setAttribute("accountId", accountId);
            session.setAttribute("role", role);

            System.out.println("Login.java: 세션에 id, role 속성 저장 완료");
            System.out.println("contextPath = " + req.getContextPath());

            // 사용자 권한 구별 if문(관리자/일반)
            if (role.equals("일반")) {
                resp.sendRedirect(req.getContextPath() + "/index.do");
            } else if (role.equals("관리자")) {
                resp.sendRedirect(req.getContextPath() + "/admin/admin.do");
            }

            // resp.sendRedirect(req.getContextPath() + "/index.do");

        } else {
            req.setAttribute("error", "아이디 혹은 비밀번호가 일치하지 않습니다.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/login.jsp");
            dispatcher.forward(req, resp);
        }

        dao.close();
    }
}
