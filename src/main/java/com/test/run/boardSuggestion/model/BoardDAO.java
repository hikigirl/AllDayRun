package com.test.run.boardSuggestion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * BoardDAO 클래스
 * 게시판 관련 데이터베이스 작업 메서드 담은 클래스
 */
public class BoardDAO {

	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	/**
	 * BoardDAO() 생성자
	 */
	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			DataSource ds = (DataSource) env.lookup("jdbc/pool");

			conn = ds.getConnection();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Connection 닫기
	 */
	public void close() {
		try {
			this.conn.close();
		} catch (Exception e) {
			// handle exception
			System.out.println("BoardDAO.close()");
			e.printStackTrace();
		}
	}

	/**
	 * 게시글 등록
	 * 
	 * @param BoardDTO dto
	 * @return 성공시 1 반환, 실패시 0 반환
	 */
	public int add(BoardDTO dto) {

		// queryParamNoReturn
		try {

			String sql = "insert into tblBoardSuggestion (boardContentSeq, accountId, title, content, attach, regdate, readCount, favoriteCount, boardContentTypeSeq) values (boardContentSeq.nextVal, ?, ?, ?, ?, default, default, default, ?)";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getAccountId());
			pstat.setString(2, dto.getTitle());
			pstat.setString(3, dto.getContent());
			pstat.setString(4, dto.getAttach());
			pstat.setDouble(5, dto.getBoardContentTypeSeq());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 게시판 목록 출력
	 * 
	 * @return 게시글 목록을 담은 ArrayList 반환, 없을 시 null 반환
	 */
	public List<BoardDTO> list() {

		// queryNoParamListReturn
		try {

			String sql = "select tblBoardSuggestion.*, (select name from tblAccountInfoDetail where accountId = tblBoardSuggestion.accountId) as name from tblBoardSuggestion order by regdate desc";

			stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();

			while (rs.next()) {

				BoardDTO dto = new BoardDTO();

				dto.setBoardContentSeq(rs.getString("boardContentSeq"));
				dto.setAccountId(rs.getString("accountId"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setAttach(rs.getString("attach"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setReadCount(rs.getInt("readCount"));
				dto.setFavoriteCount(rs.getInt("favoriteCount"));
				dto.setBoardContentTypeSeq(rs.getInt("boardContentTypeSeq"));

				dto.setName(rs.getString("name"));

				list.add(dto);
			}

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 특정 게시글을 조회한다.
	 * 
	 * @param boardContentSeq 조회할 게시글의 고유 번호
	 * @return 조회된 게시글 정보를 담은 BoardDTO 객체. 해당하는 게시글이 없으면 null 반환.
	 */
	public BoardDTO get(String boardContentSeq) {

		// queryParamDTOReturn
		try {
			String sql = "select tblBoardSuggestion.*, (select name from tblAccountInfoDetail where accountId = tblBoardSuggestion.accountId) as name from tblBoardSuggestion where boardContentSeq = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, boardContentSeq);
			rs = pstat.executeQuery();

			if (rs.next()) {

				BoardDTO dto = new BoardDTO();
				dto.setBoardContentSeq(rs.getString("boardContentSeq"));
				dto.setAccountId(rs.getString("accountId"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setAttach(rs.getString("attach"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setReadCount(rs.getInt("readCount"));
				dto.setFavoriteCount(rs.getInt("favoriteCount"));
				dto.setBoardContentTypeSeq(rs.getInt("boardContentTypeSeq"));

				dto.setName(rs.getString("name"));

				return dto;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 게시글을 수정한다.
	 * 
	 * @param dto 수정할 게시글 정보 (제목, 내용 포함)
	 * @return 수정 성공시 1, 실패시 0 반환
	 */
	public int edit(BoardDTO dto) {

		// queryParamNoReturn
		try {

			String sql = "update tblBoardSuggestion set title = ?, content = ? where boardContentSeq = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getTitle());
			pstat.setString(2, dto.getContent());
			pstat.setString(3, dto.getBoardContentSeq());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 게시글을 삭제한다.
	 * 
	 * @param boardContentSeq 삭제할 게시글의 고유 번호
	 * @return 삭제 성공시 1, 실패시 0 반환
	 */
	public int del(String boardContentSeq) {

		// queryParamNoReturn
		try {
			String sql = "delete from tblBoardSuggestion where boardContentSeq = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, boardContentSeq);

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

}
