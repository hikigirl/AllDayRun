package com.test.run;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(value = "/connectiontest.do")
public class ConnectionTest extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//ConnectionTest.java
		try {
			Connection conn = null;
			Statement stat = null;
			ResultSet rs = null;
			
			//context.xml 읽기 -> JNDI
			Context ctx = new InitialContext(); //javax.naming 패키지
			Context env = (Context)ctx.lookup("java:comp/env");
			
			DataSource ds = (DataSource)env.lookup("jdbc/pool"); //javax.sql 패키지
			
			//DB 연결(DBUtil.open()과 같은 상황)
			conn = ds.getConnection();
			System.out.println(conn.isClosed()); //false
			
			stat = conn.createStatement();
			rs = stat.executeQuery("select count(*) as cnt from tblAccountInfo");
			if(rs.next()) {
				System.out.println(rs.getInt("cnt"));
			}
			
			//close()의 의미: 
			//커넥션 풀을 사용하지 않으면 - 연결을 끊음
			//커넥션 풀을 사용하면		  - 연결을 끊지 않고 풀에 반납
			conn.close();
			
			
			
		} catch (Exception e) {
			// handle exception
			System.out.println("ConnectionTest.doGet()");
			e.printStackTrace();
		}
		
	}
}