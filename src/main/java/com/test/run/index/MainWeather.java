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

@WebServlet("/weather.do")
public class MainWeather extends HttpServlet {

    // ⚠️ 네 OpenWeather 3.0 API 키 입력
    private static final String API_KEY = "7de86ecde5931bd86677b812af5d0d4f";

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
            API_KEY
        );

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
            while ((line = reader.readLine()) != null) sb.append(line);

            resp.setStatus(status);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write(sb.toString());

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("OpenWeather 호출 실패: " + e.getMessage());
        } finally {
            if (reader != null) reader.close();
            if (conn != null) conn.disconnect();
        }
    }
}
