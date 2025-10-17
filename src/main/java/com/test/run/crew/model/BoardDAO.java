package com.test.run.crew.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.test.run.crew.util.DBUtil;

public class BoardDAO {

	private Connection conn;
	private PreparedStatement pstat;
	private ResultSet rs;

	public BoardDAO() {
		conn = DBUtil.getConnection();

	}

	public List<BoardDTO> list(HashMap<String, String> map, String crewSeq) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		try {
			String sql = "SELECT * FROM (SELECT b.*, a.NICKNAME, ROWNUM AS rnum FROM tblCrewBoardGeneral b LEFT JOIN tblAccountInfo a ON b.ACCOUNTID = a.ACCOUNTID WHERE b.crewSeq = ? ORDER BY b.BOARDCONTENTSEQ DESC) WHERE rnum BETWEEN ? AND ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, crewSeq);
			pstat.setString(2, map.get("begin"));
			pstat.setString(3, map.get("end"));

			rs = pstat.executeQuery();

			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				                dto.setBoardContentSeq(rs.getInt("BOARDCONTENTSEQ"));
								dto.setCrewSeq(rs.getInt("CREWSEQ"));
								dto.setAccountId(rs.getString("ACCOUNTID"));
								dto.setTitle(rs.getString("TITLE"));
								dto.setContent(rs.getString("CONTENT"));
								dto.setAttach(rs.getString("ATTACH"));
								dto.setRegdate(rs.getString("REGDATE"));
								dto.setReadCount(rs.getInt("READCOUNT"));
								dto.setFavoriteCount(rs.getInt("FAVORITECOUNT"));
								dto.setBoardContentTypeSeq(rs.getInt("BOARDCONTENTTYPESEQ"));
								dto.setNickname(rs.getString("NICKNAME"));
				
								list.add(dto);			}

		} catch (Exception e) {
			System.out.println("BoardDAO.list()");
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

	public void updateReadCount(String boardContentSeq) {
		try {
			String sql = "UPDATE tblCrewBoardGeneral SET readCount = readCount + 1 WHERE boardContentSeq = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, boardContentSeq);
			pstat.executeUpdate();
		} catch (Exception e) {
			System.out.println("BoardDAO.updateReadCount()");
			e.printStackTrace();
		}

	}

	public BoardDTO get(String boardContentSeq) {

		try {
			String sql = "SELECT b.*, a.NICKNAME FROM tblCrewBoardGeneral b LEFT JOIN tblAccountInfo a ON b.ACCOUNTID = a.ACCOUNTID WHERE b.BOARDCONTENTSEQ = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, boardContentSeq);
			rs = pstat.executeQuery();

			if (rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setBoardContentSeq(rs.getInt("BOARDCONTENTSEQ"));
				dto.setCrewSeq(rs.getInt("CREWSEQ"));
				dto.setAccountId(rs.getString("ACCOUNTID"));
				dto.setTitle(rs.getString("TITLE"));
				dto.setContent(rs.getString("CONTENT"));
				dto.setAttach(rs.getString("ATTACH"));
				dto.setRegdate(rs.getString("REGDATE"));
				dto.setReadCount(rs.getInt("READCOUNT"));
				dto.setFavoriteCount(rs.getInt("FAVORITECOUNT"));
				dto.setBoardContentTypeSeq(rs.getInt("BOARDCONTENTTYPESEQ"));
				dto.setNickname(rs.getString("NICKNAME"));
				return dto;
			}

		} catch (Exception e) {
			System.out.println("BoardDAO.get()");
			e.printStackTrace();
		}

		return null;
	}

	public int add(BoardDTO dto) {

		try {
			String sql = "INSERT INTO tblCrewBoardGeneral VALUES (seqCrewboardGeneral.nextVal,?,?,?,?,?,sysdate,0,0,?)";

			pstat = conn.prepareStatement(sql);

			pstat.setInt(1, dto.getCrewSeq());
			pstat.setString(2, dto.getAccountId());
			pstat.setString(3, dto.getTitle());
			pstat.setString(4, dto.getContent());
			pstat.setString(5, dto.getAttach());
			pstat.setInt(6, dto.getBoardContentTypeSeq());

			return pstat.executeUpdate();

		} catch (Exception e) {
			System.out.println("BoardDAO.add()");
			e.printStackTrace();
		}
		return 0;
	}

	public int getTotalCount(String crewSeq) {
		try {

			String sql = "SELECT count(*) as cnt FROM tblCrewBoardGeneral WHERE crewSeq = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, crewSeq);
			rs = pstat.executeQuery();

			if (rs.next()) {
				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			System.out.println("BoardDAO.getTotalCount()");
			e.printStackTrace();
		}
		return 0;
	}

	public List<BoardDTO> getBoardPhotosByCrewSeq(String crewSeq) {
	    List<BoardDTO> list = new ArrayList<>();
	    try {
	        String sql = "SELECT BOARDCONTENTSEQ, ATTACH, TITLE FROM tblCrewBoardGeneral WHERE CREWSEQ = ? AND ATTACH IS NOT NULL ORDER BY REGDATE DESC";
	        pstat = conn.prepareStatement(sql);
	        pstat.setString(1, crewSeq);
	        rs = pstat.executeQuery();

	        while (rs.next()) {
	            BoardDTO dto = new BoardDTO();
	            dto.setBoardContentSeq(rs.getInt("BOARDCONTENTSEQ"));
	            dto.setAttach(rs.getString("ATTACH"));
	            dto.setTitle(rs.getString("TITLE"));
	            list.add(dto);
	        }

	    } catch (Exception e) {
	        System.out.println("BoardDAO.getBoardPhotosByCrewSeq()");
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstat != null) pstat.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return list;
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

	public void updateLike(String boardContentSeq) {
		try {
			String sql = "UPDATE tblCrewBoardGeneral SET favoriteCount = favoriteCount + 1 WHERE boardContentSeq = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, boardContentSeq);

			pstat.executeUpdate();

		} catch (Exception e) {
			System.out.println("BoardDAO.updateLike()");
			e.printStackTrace();
		} finally {
			try {
				if (pstat != null)
					pstat.close();
			} catch (Exception e) {
				System.out.println("BoardDAO.updateLike()");
				e.printStackTrace();
			}
		}
	}

	public int update(BoardDTO dto) {
		try {
			String sql = "UPDATE tblCrewBoardGeneral SET title = ?, content = ?, attach = ? where boardContentSeq = ?";
			pstat = conn.prepareStatement(sql);

			pstat.setString(1, dto.getTitle());
			pstat.setString(2, dto.getContent());
			pstat.setString(3, dto.getAttach());
			pstat.setInt(4, dto.getBoardContentSeq());

			return pstat.executeUpdate();

		} catch (Exception e) {
			System.out.println("BoardDAO.update()");
			e.printStackTrace();
		}
		return 0;
	}

	public int remove(String boardContentSeq) {

		try {
			String sql = "DELETE FROM tblCrewBoardGeneral where boardContentSeq = ?";
			pstat = conn.prepareStatement(sql);

			pstat.setInt(1, Integer.parseInt(boardContentSeq));
			return pstat.executeUpdate();

		} catch (Exception e) {
			System.out.println("BoardDAO.remove()");
			e.printStackTrace();
		} finally {

			try {
				if (pstat != null) {
					pstat.close();
				}
			} catch (Exception e) {
				System.out.println("BoardDAO.remove()");
				e.printStackTrace();
			}

		}
		return 0;

	}

}
