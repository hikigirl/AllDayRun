package com.test.run.crew.util;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * 데이터베이스 연결을 관리하는 유틸리티 클래스
 * JNDI를 사용하여 데이터베이스 커넥션 풀에서 Connection 객체를 얻어오는 기능을 제공한다.
 */
public class DBUtil {

	/**
	 * 데이터베이스 커넥션 풀에서 Connection 객체를 가져온다.
	 * 
	 * @return 데이터베이스 연결 객체 (Connection)
	 */
	public static Connection getConnection() {

		Connection conn = null;

		try {
			Context initCtx = new InitialContext();
			DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/pool");
			conn = ds.getConnection();
			System.out.println("연결성공");
		} catch (Exception e) {
			System.out.println("DBUtil.getConnection()");
			e.printStackTrace();
		}

		return conn;
	}

}
