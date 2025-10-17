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


public class BoardDAO {

	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	public BoardDAO() {
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
			System.out.println("BoardDAO.close()");
			e.printStackTrace();
		}
	}
	
	//게시글 등록
	public int add(BoardDTO dto) {
		
		//queryParamNoReturn
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
	
	//게시판 리스트 출력
	public List<BoardDTO> list() {
		
		//queryNoParamListReturn
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

	
	public BoardDTO get(String boardContentSeq) {
		
		//queryParamDTOReturn
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
	
	public int edit(BoardDTO dto) {
		
		//queryParamNoReturn
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

	public int del(String boardContentSeq) {

		//queryParamNoReturn
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




