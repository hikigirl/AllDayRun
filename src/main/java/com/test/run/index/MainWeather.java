package com.test.run.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OpenWeatherMap API를 사용하여 날씨 정보를 조회하는 서블릿
 * 클라이언트로부터 위도(lat)와 경도(lon)를 받아 해당 위치의 날씨 데이터를 JSON 형태로 반환한다.
 */
@WebServlet("/weather.do")
public class MainWeather extends HttpServlet {

    // OpenWeather 3.0 API 키 입력
    private static final String API_KEY = "7de86ecde5931bd86677b812af5d0d4f";

    /**
     * GET 요청을 처리하여 OpenWeatherMap API로부터 날씨 데이터를 가져온다.
     * 'lat' (위도)과 'lon' (경도) 필수
     * 성공 시 날씨 정보를 JSON 형태로 반환, 실패 시 적절한 오류 메시지를 반환한다.
     * 
     * @param req  HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 관련 오류가 발생한 경우
     * @throws IOException      I/O 오류가 발생한 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String lat = req.getParameter("lat");
        String lon = req.getParameter("lon");

        if (lat == null || lon == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("Missing lat/lon");
            return;
        }

        // 🌍 One Call 3.0 API URL
        String apiUrl = String.format(
                "https://api.openweathermap.org/data/3.0/onecall?lat=%s&lon=%s&units=metric&lang=kr&appid=%s",
                URLEncoder.encode(lat, "UTF-8"),
                URLEncoder.encode(lon, "UTF-8"),
                API_KEY);

        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);

            resp.setStatus(status);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write(sb.toString());

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("OpenWeather 호출 실패: " + e.getMessage());
        } finally {
            if (reader != null)
                reader.close();
            if (conn != null)
                conn.disconnect();
        }
    }
}
