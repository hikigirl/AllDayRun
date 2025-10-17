package com.test.run.user;

import javax.servlet.*;
import javax.servlet.http.*;

import com.test.run.user.model.AccountDAO;
import com.test.run.user.model.AccountDTO;

import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/user/login.do")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/login.jsp");
        dispatcher.forward(req, resp);
    }
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
            
            
            //사용자 권한 구별 if문(관리자/일반)
            if (role.equals("일반")) {
                resp.sendRedirect(req.getContextPath() + "/index.do");
            } else if (role.equals("관리자")) {
                resp.sendRedirect(req.getContextPath() + "/admin/admin.do");
            }
            
            //resp.sendRedirect(req.getContextPath() + "/index.do");

        } else {
            req.setAttribute("error", "아이디 혹은 비밀번호가 일치하지 않습니다.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/login.jsp");
            dispatcher.forward(req, resp);
        }
        
        dao.close();
    }
}
