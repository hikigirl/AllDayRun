package com.test.run.crew.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.run.crew.util.DBUtil;

/**
 * 크루 게시판 댓글 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * 댓글 목록 조회 및 댓글 추가 기능을 제공한다.
 */
public class CommentDAO {

	private Connection conn;
	private PreparedStatement pstat;
	private ResultSet rs;

	/**
	 * CommentDAO의 생성자. 데이터베이스 연결을 초기화한다.
	 */
	public CommentDAO() {

		conn = DBUtil.getConnection();

	}

	/**
	 * 데이터베이스 연결을 닫는다.
	 */
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

	/**
	 * 특정 게시글에 달린 댓글 목록을 조회한다.
	 * 
	 * @param boardContentSeq 댓글을 조회할 게시글의 번호(Primary Key)
	 * @return 조회된 댓글 목록 (CommentDTO 객체 리스트)
	 */
	public List<CommentDTO> commentList(String boardContentSeq) {

		List<CommentDTO> list = new ArrayList<CommentDTO>();

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

	/**
	 * 새로운 댓글을 데이터베이스에 추가한다.
	 * 
	 * @param dto 추가할 댓글 정보 (CommentDTO 객체)
	 * @return 추가된 행의 수 (성공 시 1, 실패 시 0 반환)
	 */
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
				if (pstat != null) {
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
