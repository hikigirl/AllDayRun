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
 * 사용자 회원가입의 첫 번째 단계를 처리하는 클래스
 * 이메일 인증 코드 검증 및 비밀번호 설정 기능을 제공
 * 이메일 발송은 {@code /user/sendmail.do}에서 Ajax로 수행하며, 성공 시 세션에 "validNumber"를 저장한다.
 */
@WebServlet("/user/registerstep1.do")
public class RegisterStep1 extends HttpServlet {

    /**
     * GET 요청을 처리한다.
     * 회원가입 1단계(이메일 인증 및 비밀번호 설정) 페이지를 클라이언트에게 반환한다.
     * 세션에 저장된 사용자 정보를 요청 속성으로 설정한다.
     *
     * @param req  클라이언트로부터의 HttpServletRequest 객체
     * @param resp 클라이언트로의 HttpServletResponse 객체
     * @throws ServletException 서블릿 관련 오류
     * @throws IOException      입출력 오류
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        req.setAttribute("nickname", session.getAttribute("reg_nickname"));
        req.setAttribute("photoName", session.getAttribute("reg_profilePhoto"));
        req.setAttribute("registerType", session.getAttribute("reg_registerType"));

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/user/registerstep1.jsp");
        rd.forward(req, resp);
    }

    /**
     * POST 요청을 처리한다.
     * 이메일 인증 코드 검증 및 비밀번호 유효성 검사를 수행
     * <p>
     * {@code action} 파라미터 값에 따라 다음 두 가지 작업을 수행한다.
     * <ul>
     * <li>{@code verifyCode}: 사용자가 입력한 인증 코드를 세션에 저장된 코드와 비교하여 이메일 인증을 확인</li>
     * <li>{@code submit}: 사용자가 입력한 비밀번호의 유효성을 검사하고, 이메일 인증이 완료된 경우 다음 회원가입 단계로
     * 리다이렉트</li>
     * </ul>
     * </p>
     *
     * @param req  클라이언트로부터의 HttpServletRequest 객체
     * @param resp 클라이언트로의 HttpServletResponse 객체
     * @throws ServletException 서블릿 관련 오류
     * @throws IOException      입출력 오류
     */
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
            String confirm = req.getParameter("confirm");
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

    /**
     * 요청과 응답 객체를 {@code /WEB-INF/views/user/registerstep1.jsp}로 포워드
     *
     * @param req  클라이언트로부터의 HttpServletRequest 객체
     * @param resp 클라이언트로의 HttpServletResponse 객체
     * @throws ServletException 서블릿 관련 오류
     * @throws IOException      입출력 오류
     */
    private void forward(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/user/registerstep1.jsp");
        rd.forward(req, resp);
    }
}