package com.test.run.user;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@WebServlet("/user/googleoauthcallback.do")
public class GoogleOAuthCallback extends HttpServlet {

    private static final String CLIENT_ID = "클라이언트 ID";
    private static final String CLIENT_SECRET = "클라이언트 시크릿"; // 클라이언트 시크릿 입력
    private static final String REDIRECT_URI = "http://localhost:8080/user/googleoauthcallback.do";
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String code = req.getParameter("code");

        if (code == null || code.isEmpty()) {
            resp.getWriter().write("❌ 구글 로그인 실패: 인증 코드가 없습니다.");
            return;
        }

        try {
            // 1️⃣ Access Token 요청
            String tokenUrl = "https://oauth2.googleapis.com/token";
            String body = "code=" + URLEncoder.encode(code, "UTF-8")
                    + "&client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8")
                    + "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8")
                    + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8")
                    + "&grant_type=authorization_code";

            URL url = new URL(tokenUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes("UTF-8"));
            }

            InputStream is = conn.getResponseCode() == 200
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            String tokenResponse = new BufferedReader(new InputStreamReader(is))
                    .lines().reduce("", (a, b) -> a + b);

            JsonObject tokenJson = JsonParser.parseString(tokenResponse).getAsJsonObject();
            String accessToken = tokenJson.get("access_token").getAsString();

            // 2️⃣ 사용자 정보 요청
            URL infoUrl = new URL("https://www.googleapis.com/oauth2/v2/userinfo");
            HttpURLConnection infoConn = (HttpURLConnection) infoUrl.openConnection();
            infoConn.setRequestMethod("GET");
            infoConn.setRequestProperty("Authorization", "Bearer " + accessToken);

            InputStream infoStream = infoConn.getInputStream();
            String userInfoResponse = new BufferedReader(new InputStreamReader(infoStream))
                    .lines().reduce("", (a, b) -> a + b);

            JsonObject userJson = JsonParser.parseString(userInfoResponse).getAsJsonObject();

            String email = userJson.get("email").getAsString();
            String name = userJson.has("name") ? userJson.get("name").getAsString() : "구글사용자";
            String profilePhoto = userJson.has("picture") ? userJson.get("picture").getAsString() : "pic.png";

            // 세션에 정보 저장 (회원가입 2단계에서 사용)
            HttpSession session = req.getSession();
            session.setAttribute("reg_accountId", email);
            session.setAttribute("reg_nickname", name);
            session.setAttribute("reg_profilePhoto", profilePhoto);
            session.setAttribute("reg_registerType", "소셜");

            // 3️⃣ RegisterStep2로 이동
            resp.sendRedirect(req.getContextPath() + "/user/registerstep2.do");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("❌ 구글 OAuth 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
