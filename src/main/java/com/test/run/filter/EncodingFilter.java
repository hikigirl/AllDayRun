package com.test.run.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 모든 요청과 응답에 대해 문자 인코딩을 UTF-8로 설정하는 서블릿 필터.
 * 한글 깨짐 현상을 방지하고 일관된 문자 처리를 보장함.
 */
// 톰캣이 관리
public class EncodingFilter implements Filter {

	/**
	 * 필터 초기화 메서드입니다. 현재는 특별한 초기화 작업을 수행하지 않음.
	 * 
	 * @param filterConfig 필터 설정을 위한 FilterConfig 객체
	 * @throws ServletException 서블릿 관련 오류가 발생한 경우
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// System.out.println("필터 생성");
	}

	/**
	 * 필터의 핵심 로직을 수행하는 메서드
	 * 들어오는 요청(ServletRequest)과 나가는 응답(ServletResponse)의 문자 인코딩을 UTF-8로 설정한다.
	 * 이후 필터 체인의 다음 필터 또는 최종 목적지 서블릿/JSP로 요청을 전달
	 * 
	 * @param req   ServletRequest 객체
	 * @param resp  ServletResponse 객체
	 * @param chain FilterChain 객체
	 * @throws IOException      I/O 오류가 발생한 경우
	 * @throws ServletException 서블릿 관련 오류가 발생한 경우
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		// System.out.println("필터 동작");
		// System.out.println("로그 기록");
		// System.out.println(((HttpServletRequest)req).getRequestURI());

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		// 마지막 > 다음 필터 or 서블릿 > 호출
		chain.doFilter(req, resp);

	}

	/**
	 * 필터 소멸 메서드
	 * 현재는 특별한 소멸 작업을 수행하지 않음
	 */
	@Override
	public void destroy() {
		// System.out.println("필터 소멸");
	}

}
