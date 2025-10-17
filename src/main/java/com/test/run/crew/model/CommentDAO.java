package com.test.run.crew.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.run.crew.util.DBUtil;

public class CommentDAO {
	
	private Connection conn;
	private PreparedStatement pstat;
	private ResultSet rs;

	public CommentDAO() {
		
		conn = DBUtil.getConnection();
		
	}
	
	public void close() {
		try {
			if (this.conn != null) {
				this.conn.close();
			}
		} catch (Exception e) {
			System.out.println("BoardDAO.close()");
			e.printStackTrace();
		}

	}
	
	public List<CommentDTO> commentList(String boardContentSeq) {
		
		List< CommentDTO> list = new ArrayList<CommentDTO>();
		
		try {
			String sql = "SELECT c.*, a.NICKNAME FROM tblCrewCommentGeneral c LEFT JOIN tblAccountInfo a ON c.ACCOUNTID = a.ACCOUNTID WHERE c.BOARDCONTENTSEQ = ? ORDER BY c.REGDATE DESC";
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, boardContentSeq);
			
			rs = pstat.executeQuery();
			
			while (rs.next()) {
				
				CommentDTO dto = new CommentDTO();
				
				dto.setCommentSeq(rs.getInt("COMMENTSEQ"));
				dto.setCrewSeq(rs.getInt("CREWSEQ"));
				dto.setBoardContentSeq(rs.getInt("BOARDCONTENTSEQ"));
				dto.setAccountId(rs.getString("ACCOUNTID"));
				dto.setContent(rs.getString("CONTENT"));
				dto.setRegdate(rs.getString("REGDATE"));
				dto.setNickname(rs.getString("NICKNAME"));
				
				list.add(dto);
				
			}
			
		} catch (Exception e) {
			System.out.println("CommentDAO.commentList()");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstat != null)
					pstat.close();
			} catch (Exception e) {
			}
		}
		// 오류 발생 시 null 대신 빈 리스트를 반환하여 안정성 높임
		return list;
	
	}

	public int add(CommentDTO dto) {
		
		try {
			
			String sql = "INSERT INTO tblCrewCommentGeneral VALUES (seqCrewCommentGeneral.nextVal, ?, ?, ?, ?, sysdate)";
			pstat = conn.prepareStatement(sql);
			
			pstat.setInt(1, dto.getCrewSeq());
			pstat.setInt(2, dto.getBoardContentSeq());
			pstat.setString(3, dto.getAccountId());
			pstat.setString(4, dto.getContent());
			
			return pstat.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("CommentDAO.add()");
			e.printStackTrace();
		} finally {
			
			try {
				if(pstat != null) {
					pstat.close();
				}
			} catch (Exception e) {
				System.out.println("CommentDAO.add()");
				e.printStackTrace();
			}
		}
		return 0;
	}
}
