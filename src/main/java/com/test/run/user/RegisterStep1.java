package com.test.run.user;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
/**
 * RegisterStep1
 * - 이메일 인증(코드 발송은 /user/sendmail.do에서 Ajax로 수행)
 * - 인증코드 검증
 * - 비밀번호 규칙 검증 및 다음 단계로 이동
 *
 * 전제:
 * 1) 메일 발송은 SendMail 서블릿(/user/sendmail.do)에서 처리하며,
 *    성공 시 세션에 "validNumber" 라는 이름으로 정수 코드가 저장됨.
 * 2) 이 서블릿은 코드 검증(action=verifyCode)과 제출(action=submit)만 담당한다.
 */
@WebServlet("/user/registerstep1.do")
public class RegisterStep1 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	HttpSession session = req.getSession();
    	
    	req.setAttribute("nickname", session.getAttribute("reg_nickname"));
    	req.setAttribute("photoName", session.getAttribute("reg_profilePhoto"));
    	req.setAttribute("registerType", session.getAttribute("reg_registerType"));

        RequestDispatcher rd =
            req.getRequestDispatcher("/WEB-INF/views/user/registerstep1.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();

        String action = req.getParameter("action"); // verifyCode | submit

        // 1) 인증번호 확인
        if ("verifyCode".equals(action)) {
            String input = req.getParameter("code");

            Object savedObj = session.getAttribute("validNumber"); // SendMail에서 넣어둠(정수)
            String saved = savedObj == null ? null : String.valueOf(savedObj);

            if (saved != null && saved.equals(input)) {
                session.setAttribute("emailVerified", true);
                
                String email = req.getParameter("email");
                if (email != null && !email.isEmpty()) {
                    session.setAttribute("reg_accountId", email);
                    req.setAttribute("emailValue", email);
                }
                
                req.setAttribute("codeValue", input);
                req.setAttribute("emailMsg", "이메일 인증이 완료되었습니다.");
            } else {
                session.setAttribute("emailVerified", false);
                req.setAttribute("emailMsg", "인증번호가 올바르지 않습니다.");
            }

            forward(req, resp);
            return;
        }

        // 2) 최종 제출(비밀번호 검증)
        if ("submit".equals(action)) {
            String password = req.getParameter("password");
            String confirm  = req.getParameter("confirm");
            boolean verified = Boolean.TRUE.equals(session.getAttribute("emailVerified"));
            
            System.out.println(">>> verified = " + verified);
            System.out.println(">>> email = " + req.getParameter("email"));


            // 비밀번호 규칙: 8~16, 영대소문자/숫자/! ? # $ & 만 허용
            boolean pwOk = password != null &&
                    password.matches("^[A-Za-z0-9!?#$&]{8,16}$");

            if (!verified) {
                req.setAttribute("emailMsg", "이메일 인증을 완료해 주세요.");
            }
            if (!pwOk) {
                req.setAttribute("pwMsg", "비밀번호에 사용할 수 없는 문자가 포함되었거나 길이가 올바르지 않습니다.");
            } else if (!password.equals(confirm)) {
                req.setAttribute("confirmMsg", "비밀번호와 일치하지 않습니다.");
            }

            if (verified && pwOk && password.equals(confirm)) {
                // 다음 단계로 넘길 준비(필요 시 세션 저장)
            	
            	String email = (String) session.getAttribute("reg_accountId");
            	session.setAttribute("reg_password", req.getParameter("password"));
                // 예시: step2로 이동
                resp.sendRedirect(req.getContextPath() + "/user/registerstep2.do");
                return;
            }

            forward(req, resp);
            return;
        }

        // 그 외
        forward(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher rd =
            req.getRequestDispatcher("/WEB-INF/views/user/registerstep1.jsp");
        rd.forward(req, resp);
    }
}
