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
	public int addCourseTransaction(String courseName, String accountId, List<SpotDTO> spots) {
		// 
		try {
			// --- 트랜잭션 시작 ---
	        conn.setAutoCommit(false); //수동커밋으로 변경
	        long newCourseSeq = 0;
	        
	        //1. tblCourse에 코스 정보 insert
	        String courseSql = "INSERT INTO tblCourse(courseSeq, courseName, courseApproval, accountId) VALUES (seqCourse.nextVal, ?, default, ?)";
            String generatedCourseColumns[] = { "courseSeq" }; // 생성된 키를 반환받기 위함
            pstat = conn.prepareStatement(courseSql, generatedCourseColumns);
            
            pstat.setString(1, courseName);
            pstat.setString(2, accountId); 
            pstat.executeUpdate();
            
            // 방금 INSERT된 courseSeq 가져오기
            ResultSet courseKeys = pstat.getGeneratedKeys();
            if (courseKeys.next()) {
                newCourseSeq = courseKeys.getLong(1);
            }
            // courseSeq를 받지 못했다면 롤백
            if (newCourseSeq == 0) {
                conn.rollback();
                return 0;
            }
            
            //2. tblSpot에 모든 지점 등록
            List<Long> spotSeqs = new ArrayList<>(); // 생성된 spotSeq들을 순서대로 저장할 리스트
            String spotSql = "INSERT INTO tblSpot(spotSeq, place, lat, lng, courseSeq, spotStep) VALUES (seqSpot.nextVal, ?, ?, ?, ?, ?)";
            String generatedSpotColumns[] = { "spotSeq" };
            pstat = conn.prepareStatement(spotSql, generatedSpotColumns);
            
            for (int i = 0; i < spots.size(); i++) {
                SpotDTO dto = spots.get(i);
                
                pstat.setString(1, dto.getPlace());
                pstat.setString(2, dto.getLat());
                pstat.setString(3, dto.getLng());
                pstat.setLong(4, newCourseSeq);     // tblCourse FK
                pstat.setInt(5, i);                 // 순서 (0부터 시작)
                pstat.executeUpdate();
                
                // 방금 INSERT된 spotSeq 가져오기
                ResultSet spotKeys = pstat.getGeneratedKeys();
                if (spotKeys.next()) {
                    spotSeqs.add(spotKeys.getLong(1));
                }
            }
            
            // Spot 개수와 PK 개수 불일치 시 롤백 (안전 장치)
            if (spotSeqs.size() != spots.size()) {
                conn.rollback();
                return 0;
            }
            
            // 3. tblTrack에 순서대로 경로 등록
            // tblTrack에 courseSeq 컬럼이 추가되어야 함
            // courseLength는 현재 계산이 불가능하므로 0으로 임시 저장
            String trackSql = "INSERT INTO tblTrack(trackSeq, courseSeq, startSpotSeq, endSpotSeq, courseLength) VALUES (seqTrack.nextVal, ?, ?, ?, 0)";
            pstat = conn.prepareStatement(trackSql);

            for (int i = 0; i < spotSeqs.size() - 1; i++) {
                pstat.setLong(1, newCourseSeq);             // tblCourse FK
                pstat.setLong(2, spotSeqs.get(i));          // startSpotSeq
                pstat.setLong(3, spotSeqs.get(i + 1));      // endSpotSeq
                pstat.executeUpdate();
            }

            // --- 모든 작업 성공 시 커밋 ---
            conn.commit();
            return 1; // 성공
			
		} catch (Exception e) {
			System.out.println("CourseDAO.addCourseTransaction 실패");
            e.printStackTrace();
            try {
                // 오류 발생 시 롤백
                conn.rollback();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                // 원래의 자동 커밋 상태로 복구
                conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		return 0;
	}


	
	
	
	
	
	

}