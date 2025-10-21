package com.test.run.course;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.run.course.model.CourseCardDTO;
import com.test.run.course.model.CourseDAO;
import com.test.run.course.model.PageDTO;

/**
 * 코스 목록 페이지를 처리하는 서블릿
 * 페이징 처리를 통해 코스 목록을 조회하고 JSP로 전달한다.
 */
@WebServlet(value = "/course/courselist.do")
public class CourseList extends HttpServlet {
    /**
     * HTTP GET 요청을 처리한다.
     * 페이징된 코스 목록을 조회하여 courselist.jsp로 전달한다.
     * 
     * @param req  클라이언트의 HttpServletRequest
     * @param resp 서버의 HttpServletResponse
     * @throws ServletException 서블릿 처리 중 예외가 발생할 경우
     * @throws IOException      입출력 예외가 발생할 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // CourseList.java
        req.setCharacterEncoding("UTF-8");
        CourseDAO dao = new CourseDAO();

        // 1. 현재 페이지 번호 가져오기 (없으면 1페이지로)
        String pageStr = req.getParameter("page");
        int currentPage = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;

        // 2. 페이징 계산
        int totalCount = dao.getTotalCourseCount();
        PageDTO pageDTO = new PageDTO(totalCount, currentPage);

        // 3. 현재 페이지에 해당하는 코스 목록만 DB에서 조회
        int start = ((currentPage - 1) * pageDTO.getPageSize()) + 1;
        int end = start + pageDTO.getPageSize() - 1;
        List<CourseCardDTO> courseList = dao.getAllCourses(start, end);

        // 4. JSP로 데이터 전달
        req.setAttribute("courseList", courseList); // 현재 페이지의 코스 목록
        req.setAttribute("pageDTO", pageDTO); // 페이징 정보

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/course/courselist.jsp");
        dispatcher.forward(req, resp);

        dao.close();

    }

}