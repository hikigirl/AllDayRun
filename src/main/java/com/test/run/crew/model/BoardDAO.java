package com.test.run.crew.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.test.run.crew.util.DBUtil;

/**
 * 크루 게시판 관련 데이터베이스 작업을 처리하는 DAO 클래스.
 * 게시글 목록 조회, 상세 조회, 추가, 수정, 삭제 및 조회수/좋아요 수 업데이트 기능을 제공
 */
public class BoardDAO {

	private Connection conn;
	private PreparedStatement pstat;
	private ResultSet rs;

	/**
	 * BoardDAO의 생성자. 데이터베이스 연결을 초기화한다.
	 */
	public BoardDAO() {
		conn = DBUtil.getConnection();

	}

	/**
	 * 특정 크루의 게시글 목록을 페이지네이션하여 조회
	 * 
	 * @param map     시작 및 끝 ROWNUM을 포함하는 HashMap (begin, end).
	 * @param crewSeq 조회할 크루의 번호 (Primary Key)
	 * @return 조회된 게시글 목록 (BoardDTO 객체 리스트).
	 */
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

				list.add(dto);
			}

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

	/**
	 * 특정 게시글의 조회수를 1 증가시키는 메서드
	 * 
	 * @param boardContentSeq 조회수를 증가시킬 게시글의 번호(Primary Key)
	 */
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

	/**
	 * 특정 게시글의 상세 정보를 조회
	 * 
	 * @param boardContentSeq 조회할 게시글의 번호(Primary Key)
	 * @return 조회된 게시글 정보 (BoardDTO 객체), 없으면 null 반환
	 */
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

	/**
	 * 새로운 게시글을 데이터베이스에 추가
	 * 
	 * @param dto 추가할 게시글 정보 (BoardDTO 객체)
	 * @return 추가된 행의 수 (성공 시 1, 실패 시 0 반환)
	 */
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

	/**
	 * 특정 크루의 전체 게시글 수를 조회
	 * 
	 * @param crewSeq 게시글 수를 조회할 크루의 번호(Primary Key)
	 * @return 해당 크루의 전체 게시글 수
	 */
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

	/**
	 * 특정 크루의 활동 사진 목록을 조회한다. 첨부 파일이 있는 게시글만 포함한다.
	 * 
	 * @param crewSeq 활동 사진을 조회할 크루의 번호(Primary Key)
	 * @return 활동 사진 목록 (BoardDTO 객체 리스트)
	 */
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
				if (rs != null)
					rs.close();
				if (pstat != null)
					pstat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
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
	 * 특정 게시글의 좋아요 수를 1 증가시킨다.
	 * 
	 * @param boardContentSeq 좋아요 수를 증가시킬 게시글의 번호(Primary Key)
	 */
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

	/**
	 * 기존 게시글을 수정한다.
	 * 
	 * @param dto 수정할 게시글 정보 (BoardDTO 객체)
	 * @return 수정된 행의 수 (성공 시 1, 실패 시 0 반환)
	 */
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

	/**
	 * 특정 게시글을 삭제한다.
	 * 
	 * @param boardContentSeq 삭제할 게시글의 번호(Primary Key)
	 * @return 삭제된 행의 수 (성공 시 1, 실패 시 0 반환)
	 */
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
