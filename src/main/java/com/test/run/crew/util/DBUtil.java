package com.test.run.crew.util;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class DBUtil {
	
		public static  Connection getConnection() {
			
			Connection conn = null;
			
			try {
				 Context initCtx= new InitialContext();
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
