package com.test.run.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

//톰캣이 관리
public class EncodingFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//System.out.println("필터 생성");
	}

	@Override
	public void doFilter(ServletRequest req
						, ServletResponse resp
						, FilterChain chain)
			throws IOException, ServletException {
		
		//System.out.println("필터 동작");
		//System.out.println("로그 기록");
		//System.out.println(((HttpServletRequest)req).getRequestURI());
		
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		//마지막 > 다음 필터 or 서블릿 > 호출
		chain.doFilter(req, resp);
		
	}
	
	@Override
	public void destroy() {
		//System.out.println("필터 소멸");
	}

}






