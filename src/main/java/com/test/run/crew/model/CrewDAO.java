package com.test.run.crew.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.test.run.crew.util.DBUtil;

public class CrewDAO {

	private Connection conn;
	private PreparedStatement pstat;
	private ResultSet rs;

	public CrewDAO() {

		conn = DBUtil.getConnection();

	}

	public void close() {
		try {
			if (this.conn != null) {
				this.conn.close();
			}
		} catch (Exception e) {
			System.out.println("crewDAO.close()");
			e.printStackTrace();
		}

	}

	public int add(CrewDTO dto) {
		try {
			String sql = "INSERT INTO tblCrew VALUES (seqCrew.nextVal,?,?,0,?,?,?,?,?,?,?)";
			pstat = conn.prepareStatement(sql);

			pstat.setString(1, dto.getCrewName());
			pstat.setString(2, dto.getDescription());
			pstat.setString(3, dto.getRegionCity());
			pstat.setString(4, dto.getRegionCounty());
			pstat.setString(5, dto.getRegionDistrict());
			pstat.setString(6, dto.getCrewAttach());
			pstat.setDouble(7, dto.getLatitude());
			pstat.setDouble(8, dto.getLongitude());
			// 로그인된 아이디
			pstat.setString(9, dto.getAccountId());

			int result = pstat.executeUpdate();

			if (result == 0) {
				return 0;
			}
			// 멤버등록을 위한 생성된 크루의 seq 찾기

			int newSeq = 0;
			sql = "SELECT seqCrew.CURRVAL FROM DUAL";

			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();

			if (rs.next()) {

				newSeq = rs.getInt(1);

			}

			if (newSeq == 0) {
				return 0;
			}

			// 멤버 넣기
			sql = "INSERT INTO tblCrewMember VALUES (seqCrewMember.nextVal,?,?,'리더')";

			pstat = conn.prepareStatement(sql);
			pstat.setInt(1, newSeq);
			pstat.setString(2, dto.getAccountId());
			pstat.executeUpdate();
			
			String membercountSql = "UPDATE tblCrew SET MEMBERCOUNT = MEMBERCOUNT + 1 WHERE CREWSEQ = ?";
			pstat = conn.prepareStatement(membercountSql);
			pstat.setInt(1, newSeq);
			return pstat.executeUpdate();

		} catch (Exception e) {
			System.out.println("CrewDAO.add()");
			e.printStackTrace();
		}
		return 0;
	}

	public CrewDTO get(String crewSeq) {
		try {
			String sql = "SELECT c.*, a.NICKNAME FROM tblCrew c LEFT JOIN tblAccountInfo a ON c.ACCOUNTID = a.ACCOUNTID WHERE c.CREWSEQ = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, crewSeq);
			rs = pstat.executeQuery();

			if (rs.next()) {
				CrewDTO dto = new CrewDTO();
				dto.setCrewSeq(rs.getInt("CREWSEQ"));
				dto.setCrewName(rs.getString("CREWNAME"));
				dto.setDescription(rs.getString("DESCRIPTION"));
				dto.setMemberCount(rs.getInt("MEMBERCOUNT"));
				dto.setAccountId(rs.getString("ACCOUNTID"));
				dto.setCrewAttach(rs.getString("CREWATTACH"));
				dto.setLatitude(rs.getDouble("LATITUDE"));
				dto.setLongitude(rs.getDouble("LONGITUDE"));
				dto.setRegionCity(rs.getString("REGIONCITY"));
				dto.setRegionCounty(rs.getString("REGIONCOUNTY"));
				dto.setRegionDistrict(rs.getString("REGIONDISTRICT"));
				dto.setNickname(rs.getString("NICKNAME"));
				return dto;
			}

		} catch (Exception e) {
			System.out.println("CrewDAO.get()");
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
		return null;
	}

	public boolean isUserInCrew(String accountId) {

		System.out.println("--- isUserInCrew 메소드 시작, ID: " + accountId + " ---");

		try {
			// 1. 사용자가 '크루장'인 크루가 있는지 확인
			String sql = "SELECT count(*) AS cnt FROM tblCrew WHERE accountId = ?";
			System.out.println("1. 크루장 확인 쿼리 실행...");

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, accountId);
			rs = pstat.executeQuery();

			if (rs.next()) {
				int crewLeaderCount = rs.getInt("cnt");
				System.out.println(" > tblCrew에서 '" + accountId + "'로 찾은 개수: " + crewLeaderCount);
				if (crewLeaderCount > 0) {
					System.out.println(" > 크루장으로 확인됨. true를 반환하고 종료.");
					return true;
				}
			}

			// 2. 사용자가 '크루원'인 크루가 있는지 확인
			sql = "SELECT count(*) AS cnt FROM tblCrewMember WHERE accountId = ?";
			System.out.println("2. 크루원 확인 쿼리 실행...");

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, accountId);
			rs = pstat.executeQuery();

			if (rs.next()) {
				int crewMemberCount = rs.getInt("cnt");
				System.out.println(" > tblCrewMember에서 '" + accountId + "'로 찾은 개수: " + crewMemberCount);
				if (crewMemberCount > 0) {
					System.out.println(" > 크루원으로 확인됨. true를 반환하고 종료.");
					return true;
				}
			}

		} catch (Exception e) {
			System.out.println("!!! isUserInCrew() 메소드 실행 중 예외 발생 !!!");
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

		System.out.println("--- 최종 결과: 소속된 크루 없음. false를 반환합니다. ---");
		return false;
	}

	public String getCrewSeq(String accountId) {
	    try {
	        // 1. Check if user is a crew leader
	        String sql = "SELECT crewSeq FROM tblCrew WHERE accountId = ?";
	        pstat = conn.prepareStatement(sql);
	        pstat.setString(1, accountId);
	        rs = pstat.executeQuery();

	        if (rs.next()) {
	            return rs.getString("crewSeq");
	        }

	        // 2. Check if user is a crew member
	        sql = "SELECT crewSeq FROM tblCrewMember WHERE accountId = ?";
	        pstat = conn.prepareStatement(sql);
	        pstat.setString(1, accountId);
	        rs = pstat.executeQuery();

	        if (rs.next()) {
	            return rs.getString("crewSeq");
	        }

	    } catch (Exception e) {
	        System.out.println("CrewDAO.getCrewSeq()");
	        e.printStackTrace();
	    } finally {
			try {
				if (rs != null) rs.close();
				if (pstat != null) pstat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    
	    return null; // User is not in any crew
	}

	public List<CrewDTO> listPopular() {
		List<CrewDTO> list = new ArrayList<>();

		try {
			String sql = "SELECT * FROM (SELECT c.*, a.NICKNAME, ROWNUM AS rnum FROM tblCrew c LEFT JOIN tblAccountInfo a ON c.ACCOUNTID = a.ACCOUNTID ORDER BY c.MEMBERCOUNT DESC) WHERE rnum <= 4";

			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();

			while (rs.next()) {
				CrewDTO dto = new CrewDTO();

				dto.setCrewSeq(rs.getInt("CREWSEQ"));
				dto.setCrewName(rs.getString("CREWNAME"));
				dto.setDescription(rs.getString("DESCRIPTION"));
				dto.setMemberCount(rs.getInt("MEMBERCOUNT"));
				dto.setCrewAttach(rs.getString("CREWATTACH"));
				dto.setAccountId(rs.getString("ACCOUNTID"));
				dto.setLatitude(rs.getDouble("LATITUDE"));
				dto.setLongitude(rs.getDouble("LONGITUDE"));
				dto.setRegionCity(rs.getString("REGIONCITY"));
				dto.setRegionCounty(rs.getString("REGIONCOUNTY"));
				dto.setRegionDistrict(rs.getString("REGIONDISTRICT"));
				dto.setNickname(rs.getString("NICKNAME"));

				list.add(dto);
			}

			return list;

		} catch (Exception e) {
			System.out.println("CrewDAO.listPopular()");
			e.printStackTrace();
		}

		return list;
	}

	public List<CrewDTO> listNearby(double userLat, double userLng) {
		List<CrewDTO> list = new ArrayList<>();

		try {
			String sql = "SELECT * FROM (SELECT c.*, a.NICKNAME, (6371 * ACOS(COS(?*(ACOS(-1)/180)) * COS(c.LATITUDE*(ACOS(-1)/180)) * COS(c.LONGITUDE*(ACOS(-1)/180) - ?*(ACOS(-1)/180)) + SIN(?*(ACOS(-1)/180)) * SIN(c.LATITUDE*(ACOS(-1)/180)))) AS DISTANCE FROM tblCrew c LEFT JOIN tblAccountInfo a ON c.ACCOUNTID = a.ACCOUNTID ORDER BY DISTANCE ASC) WHERE ROWNUM <= 8";

			pstat = conn.prepareStatement(sql);
			pstat.setDouble(1, userLat);
			pstat.setDouble(2, userLng);
			pstat.setDouble(3, userLat);

			rs = pstat.executeQuery();

			while (rs.next()) {
				CrewDTO dto = new CrewDTO();

				dto.setCrewSeq(rs.getInt("CREWSEQ"));
				dto.setCrewName(rs.getString("CREWNAME"));
				dto.setDescription(rs.getString("DESCRIPTION"));
				dto.setMemberCount(rs.getInt("MEMBERCOUNT"));
				dto.setAccountId(rs.getString("ACCOUNTID"));
				dto.setCrewAttach(rs.getString("CREWATTACH"));
				dto.setLatitude(rs.getDouble("LATITUDE"));
				dto.setLongitude(rs.getDouble("LONGITUDE"));
				dto.setRegionCity(rs.getString("REGIONCITY"));
				dto.setRegionCounty(rs.getString("REGIONCOUNTY"));
				dto.setRegionDistrict(rs.getString("REGIONDISTRICT"));

				// 계산된 거리
				double rawDistance = rs.getDouble("DISTANCE");
				dto.setDistance(String.format("%.2f km", rawDistance));
				dto.setNickname(rs.getString("NICKNAME"));

				list.add(dto);
			}

		} catch (Exception e) {
			System.out.println("CrewDAO.listNearby()");
			e.printStackTrace();
		}

		return list;
	}

	public List<CrewDTO> CrewList() {
		List<CrewDTO> list = new ArrayList<>();

		try {
			String sql = "SELECT c.*, a.NICKNAME FROM tblCrew c LEFT JOIN tblAccountInfo a ON c.ACCOUNTID = a.ACCOUNTID";

			pstat = conn.prepareStatement(sql);

			rs = pstat.executeQuery();

			while (rs.next()) {
				CrewDTO dto = new CrewDTO();

				dto.setCrewSeq(rs.getInt("CREWSEQ"));
				dto.setCrewName(rs.getString("CREWNAME"));
				dto.setDescription(rs.getString("DESCRIPTION"));
				dto.setMemberCount(rs.getInt("MEMBERCOUNT"));
				dto.setAccountId(rs.getString("ACCOUNTID"));
				dto.setCrewAttach(rs.getString("CREWATTACH"));
				dto.setLatitude(rs.getDouble("LATITUDE"));
				dto.setLongitude(rs.getDouble("LONGITUDE"));
				dto.setRegionCity(rs.getString("REGIONCITY"));
				dto.setRegionCounty(rs.getString("REGIONCOUNTY"));
				dto.setRegionDistrict(rs.getString("REGIONDISTRICT"));
				dto.setNickname(rs.getString("NICKNAME"));

				list.add(dto);
			}

		} catch (Exception e) {
			System.out.println("CrewDAO.CrewList()");
			e.printStackTrace();
		}

		return list;
	}


	public int addCrewJoinRequest(String crewSeq, String accountId) {
		try {
			String sql = "INSERT INTO tblcrewjoin (CREWJOINSEQ, CREWSEQ, ACCOUNTID, REQUESTSTATE) VALUES (SEQCREWJOIN.nextVal, ?, ?, '대기')";
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, crewSeq);
			pstat.setString(2, accountId);
			return pstat.executeUpdate();
		} catch (Exception e) {
			System.out.println("CrewDAO.addCrewJoinRequest()");
			e.printStackTrace();
		}
		return 0;
	}

	public boolean isCrewLeader(String accountId, String crewSeq) {
		try {
			String sql = "SELECT count(*) as cnt FROM tblCrewmember WHERE accountId = ? AND crewSeq = ? AND crewGrade = '리더'";
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, accountId);
			pstat.setString(2, crewSeq);
			rs = pstat.executeQuery();
			if (rs.next()) {
				int isCrewLeaderCount = rs.getInt("cnt");
				System.out.println(isCrewLeaderCount);
				if (isCrewLeaderCount > 0) {

					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("CrewDAO.isCrewLeader()");
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
		return false;
	}

	public List<CrewJoinRequestDTO> getJoinRequestsByCrewSeq(String crewSeq) {
		List<CrewJoinRequestDTO> list = new ArrayList<>();
		try {
			String sql = "SELECT cj.*, ai.nickname FROM tblcrewjoin cj INNER JOIN tblAccountInfo ai ON cj.ACCOUNTID = ai.ACCOUNTID WHERE cj.CREWSEQ = ? ORDER BY cj.CREWJOINSEQ DESC";
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, crewSeq);
			rs = pstat.executeQuery();

			while (rs.next()) {
				CrewJoinRequestDTO dto = new CrewJoinRequestDTO();
				dto.setCrewJoinSeq(rs.getString("CREWJOINSEQ"));
				dto.setCrewSeq(rs.getString("CREWSEQ"));
				dto.setAccountId(rs.getString("ACCOUNTID"));
				dto.setRequestState(rs.getString("REQUESTSTATE"));
				dto.setNickname(rs.getString("NICKNAME"));
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("CrewDAO.getJoinRequestsByCrewSeq()");
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



	// 3. 가입 승인
	public int approveJoinRequest(String crewJoinSeq) throws SQLException {
		// 1) 요청 상태 UPDATE
		String sqlUpdate = "UPDATE tblCrewJoin SET REQUESTSTATE = '승인' WHERE CREWJOINSEQ = ?";
		pstat = conn.prepareStatement(sqlUpdate);
		pstat.setString(1, crewJoinSeq);
		int result = pstat.executeUpdate();

		if (result > 0) {
			// 2) tblCrewMember에 멤버 추가
			String sqlSelect = "SELECT CREWSEQ, ACCOUNTID FROM tblCrewJoin WHERE CREWJOINSEQ = ?";
			pstat = conn.prepareStatement(sqlSelect);
			pstat.setString(1, crewJoinSeq);
			rs = pstat.executeQuery();
			if (rs.next()) {
				String crewSeq = rs.getString("CREWSEQ");
				String accountId = rs.getString("ACCOUNTID");

				String sqlInsert = "INSERT INTO tblCrewMember(CREWMEMBERSEQ, CREWSEQ, ACCOUNTID, CREWGRADE) "
						+ "VALUES (seqCrewMember.nextVal, ?, ?, '크루원')";
				pstat = conn.prepareStatement(sqlInsert);
				pstat.setString(1, crewSeq);
				pstat.setString(2, accountId);
				pstat.executeUpdate();
				
				String membercountSql = "UPDATE tblCrew SET MEMBERCOUNT = MEMBERCOUNT + 1 WHERE CREWSEQ = ?";
				pstat = conn.prepareStatement(membercountSql);
				pstat.setString(1, crewSeq);
				pstat.executeUpdate();
			}
		}

		return result;
	}

	// 4. 가입 거절
	public int rejectJoinRequest(String crewJoinSeq) throws SQLException {
		String sql = "UPDATE tblCrewJoin SET REQUESTSTATE = '거절' WHERE CREWJOINSEQ = ?";
		pstat = conn.prepareStatement(sql);
		pstat.setString(1, crewJoinSeq);
		return pstat.executeUpdate();
	}
	
	//세션에 로그인된 아이디로 crewSeq 가져오는 메서드
	
	
	
}
