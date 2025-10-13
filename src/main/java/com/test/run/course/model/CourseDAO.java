package com.test.run.course.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class CourseDAO {

	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	public CourseDAO() {
		try {
			Context ctx = new InitialContext();
			Context env = (Context)ctx.lookup("java:comp/env");
			DataSource ds = (DataSource)env.lookup("jdbc/pool");
			
			conn = ds.getConnection();
			
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void close() {
		try {
			this.conn.close();
		} catch (Exception e) {
			// handle exception
			System.out.println("CourseDAO.close()");
			e.printStackTrace();
		}
	}


	/*
	 * public int addSpot(List<CourseDTO> spots) { try {
	 * 
	 * String sql =
	 * "INSERT INTO tblSpot(spotSeq, place, lat, lng) VALUES (seqSpot.nextVal, ?, ?, ?)"
	 * ;
	 * 
	 * pstat = conn.prepareStatement(sql); pstat.setString(1, spots.getPlace());
	 * pstat.setString(2, spots.getLat()); pstat.setString(3, spots.getLng());
	 * 
	 * return pstat.executeUpdate();
	 * 
	 * } catch (Exception e) { System.out.println("CourseDAO.addSpot");
	 * e.printStackTrace(); }
	 * 
	 * return 0; }
	 */

	//courseRegister.do에서 호출
	public int addCourse(List<CourseDTO> spots) {
		// 
		try {
			// --- 트랜잭션 시작 ---
	        conn.setAutoCommit(false); //수동커밋으로 변경
	        
	        
			String sql = "INSERT INTO tblSpot(spotSeq, place, lat, lng) VALUES (seqSpot.nextVal, ?, ?, ?)";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, value);
			
			rs = pstat.executeQuery();
			
			List<CourseDTO> list = new ArrayList<CourseDTO>();
			
			while (rs.next()) {
				
				CourseDTO dto = new CourseDTO();
				
				pstat.setString(1, dto.getPlace());
				pstat.setString(2, dto.getLat());
				pstat.setString(3, dto.getLng());
				
				list.add(dto);				
			}	
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	
	
	

}