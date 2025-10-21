package com.test.run.course.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class CourseDAO {


	private Connection conn;
//	전역 변수가 아니라 개별 메서드에서 새로 선언하는게 더 안정적
//	private Statement stat;
//	private PreparedStatement pstat;
//	private ResultSet rs;

	public CourseDAO() {
		try {
			Context ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			DataSource ds = (DataSource) env.lookup("jdbc/pool");

			conn = ds.getConnection();

			// stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 자원 닫기
	public void close() {
		try {
			if (this.conn != null)
				this.conn.close();
		} catch (Exception e) {
			// handle exception
			System.out.println("CourseDAO.close()");
			e.printStackTrace();
		}
	}

	// 관리자 페이지에서 사용 예정인 메서드 2가지 추가 필요
//	adminGetPendingCourses() 	- '대기' 상태인 코스 목록을 조회(select) 
//	adminUpdatePendingCourses() - 코스의 '대기' 상태를 '승인'으로 업데이트
	public List<CourseDTO> adminGetPendingCourses() {
		Statement stat;
		//PreparedStatement pstat = null;
		ResultSet rs = null;
		// queryNoParamListReturn
		try {

			String sql = "select * from tblCourse order by courseSeq desc";

			stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			ArrayList<CourseDTO> list = new ArrayList<CourseDTO>();

			while (rs.next()) {

				CourseDTO dto = new CourseDTO();

				dto.setCourseSeq(rs.getString("courseSeq"));
				dto.setCourseName(rs.getString("courseName"));
				dto.setCourseApproval(rs.getString("courseApproval"));
				dto.setAccountId(rs.getString("accountId"));

				list.add(dto);
			}

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public int adminUpdatePendingCourses(CourseDTO dto) {
		//Statement stat;
		PreparedStatement pstat = null;
		//ResultSet rs = null;
		// queryParamNoReturn
		try {
			String sql = "update tblCourse set courseApproval = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getCourseApproval());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	 public int getPendingCount() {
			PreparedStatement pstat = null;
			ResultSet rs = null ;
		 
	        int result = 0;
	        try {
	            String sql = "SELECT COUNT(*) AS cnt FROM tblCourse WHERE courseApproval = '대기'";
	            pstat = conn.prepareStatement(sql);
	            rs = pstat.executeQuery();

	            if (rs.next()) {
	                result = rs.getInt("cnt");
	            }

	        } catch (Exception e) {
	            System.out.println("CourseDAO.getPendingCount() : " + e.toString());
	        } finally {
	            try {
	                rs.close();
	                pstat.close();
	                //conn.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        return result;
	}

// 선민 -------------------------------------------------------------------------------------------
	// courseRegister.do에서 호출
	public int addCourseTransaction(String courseName, String accountId, List<SpotDTO> spots) {
		// 모든 리소스 변수를 지역 변수로 선언하고 사용(finally에서 닫으려면 여기서 선언해야함)
		PreparedStatement pstatCourse = null;
		PreparedStatement pstatSpot = null;
		PreparedStatement pstatTrack = null;
		ResultSet rsCourse = null;
		ResultSet rsSpot = null;

		try {
			// --- 트랜잭션 시작 ---
			conn.setAutoCommit(false); // 수동커밋으로 변경
			long newCourseSeq = 0;

			// 1. tblCourse에 코스 정보 insert
			String courseSql = "INSERT INTO tblCourse(courseSeq, courseName, courseApproval, accountId) VALUES (seqCourse.nextVal, ?, default, ?)";
			String generatedCourseColumns[] = { "courseSeq" }; // 생성된 키를 반환받기 위함
			pstatCourse = conn.prepareStatement(courseSql, generatedCourseColumns);

			pstatCourse.setString(1, courseName);
			pstatCourse.setString(2, accountId);
			pstatCourse.executeUpdate();

			// 방금 INSERT된 courseSeq 가져오기
			rsCourse = pstatCourse.getGeneratedKeys();
			if (rsCourse.next()) {
				newCourseSeq = rsCourse.getLong(1);
			}
			// courseSeq를 받지 못했다면 롤백
			if (newCourseSeq == 0) {
				conn.rollback();
				return 0;
			}

			// 2. tblSpot에 모든 지점 등록
			List<Long> spotSeqs = new ArrayList<>(); // 생성된 spotSeq들을 순서대로 저장할 리스트
			String spotSql = "INSERT INTO tblSpot(spotSeq, place, lat, lng, courseSeq, spotStep) VALUES (seqSpot.nextVal, ?, ?, ?, ?, ?)";
			String generatedSpotColumns[] = { "spotSeq" };
			pstatSpot = conn.prepareStatement(spotSql, generatedSpotColumns);

			for (SpotDTO dto : spots) {
				// SpotDTO dto = spots.get(i);

				pstatSpot.setString(1, dto.getPlace());
				pstatSpot.setString(2, dto.getLat());
				pstatSpot.setString(3, dto.getLng());
				pstatSpot.setLong(4, newCourseSeq); // tblCourse FK
				pstatSpot.setInt(5, dto.getSpotStep()); // 순서 (0부터 시작)
				pstatSpot.executeUpdate();

				// 방금 INSERT된 spotSeq 가져오기
				rsSpot = pstatSpot.getGeneratedKeys();
				if (rsSpot.next()) {
					spotSeqs.add(rsSpot.getLong(1));
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
			String trackSql = "INSERT INTO tblTrack(trackSeq, courseSeq, startspotSeq, endspotSeq, courselength) VALUES (seqTrack.nextVal, ?, ?, ?, 0)";
			pstatTrack = conn.prepareStatement(trackSql);

			for (int i = 0; i < spotSeqs.size() - 1; i++) {
				pstatTrack.setLong(1, newCourseSeq); // tblCourse FK
				pstatTrack.setLong(2, spotSeqs.get(i)); // startSpotSeq
				pstatTrack.setLong(3, spotSeqs.get(i + 1)); // endSpotSeq
				pstatTrack.executeUpdate();
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
			// 사용한 리소스를 생성 역순으로 닫아준다.
			try {
				if (rsCourse != null)
					rsCourse.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (rsSpot != null)
					rsSpot.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstatCourse != null)
					pstatCourse.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstatSpot != null)
					pstatSpot.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstatTrack != null)
					pstatTrack.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 원래의 자동 커밋 상태로 복구
			try {
				conn.setAutoCommit(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return 0; // 실패
	}

	// coursemain.do에서 호출하였음
	/**
	 * 코스 검색용, 키워드로 승인된 코스를 조회한다.
	 * 
	 * @param keyword 검색어
	 * @return 검색 결과 코스 카드 정보가 담긴 리스트
	 */
	public List<CourseCardDTO> searchCourses(String keyword) {
		List<CourseCardDTO> list = new ArrayList<CourseCardDTO>();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {

			String sql = "SELECT * FROM vwCourseCards WHERE courseName LIKE ? ORDER BY favoriteCount DESC, courseSeq DESC";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, "%" + keyword + "%");

			rs = pstat.executeQuery();

			while (rs.next()) {

				CourseCardDTO dto = new CourseCardDTO();

				dto.setCourseSeq(rs.getString("courseSeq"));
				dto.setCourseName(rs.getString("courseName"));
				dto.setTotalDistance(rs.getDouble("totalDistance"));
				dto.setFavoriteCount(rs.getInt("favoriteCount"));
				dto.setCurator(rs.getString("curator"));
				dto.setAccountId(rs.getString("accountId"));

				list.add(dto);
			}

			return list;

		} catch (Exception e) {
			System.out.println("CourseDAO.searchCourses failed");
			e.printStackTrace();
		} finally {
			// 자원 반납
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstat != null)
					pstat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// coursemain.do에서 호출하였음
	/**
	 * 인기 코스 목록 조회(비회원 사용자) 스크랩순으로 정렬한다.
	 * 
	 * @return 코스 카드 정보가 담긴 리스트
	 */
	public List<CourseCardDTO> getPopularCourses(int cardCount) {
		List<CourseCardDTO> list = new ArrayList<CourseCardDTO>();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {

			String sql = "SELECT * FROM (SELECT * FROM vwCourseCards ORDER BY FAVORITECOUNT DESC, courseSeq DESC) WHERE rownum <=?";

			pstat = conn.prepareStatement(sql);
			pstat.setInt(1, cardCount);
			rs = pstat.executeQuery();

			while (rs.next()) {

				CourseCardDTO dto = new CourseCardDTO();

				dto.setCourseSeq(rs.getString("courseSeq"));
				dto.setCourseName(rs.getString("courseName"));
				dto.setTotalDistance(rs.getDouble("totalDistance"));
				dto.setFavoriteCount(rs.getInt("favoriteCount"));
				dto.setCurator(rs.getString("curator"));
				dto.setAccountId(rs.getString("accountId"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("CourseDAO.getPopularCourses 실패");
			e.printStackTrace();
		} finally {
			// 자원 반납
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstat != null)
					pstat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	// coursemain.do에서 호출하였음
	/**
	 * 개인 상세정보 테이블에서 사용자 주소를 얻어오는 메서드
	 * 
	 * @param accountId
	 * @return 사용자 주소를 담은 map
	 */
	public Map<String, String> getUserLocation(String accountId) {
		Map<String, String> locationMap = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {

			String sql = "SELECT regionCity, regionCounty, regionDistrict FROM tblAccountInfoDetail WHERE accountId = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, accountId);

			rs = pstat.executeQuery();

			if (rs.next()) {
				locationMap = new HashMap<String, String>();
				locationMap.put("city", rs.getString("regionCity"));
				locationMap.put("county", rs.getString("regionCounty"));
				locationMap.put("district", rs.getString("regionDistrict"));
			}

		} catch (Exception e) {
			System.out.println("CourseDAO.getUserLocation failed");
			e.printStackTrace();
		} finally {
			// 자원 반납
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstat != null)
					pstat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return locationMap;
	}

	// coursemain.do에서 호출하였음
	/**
	 * 사용자 주소 기반으로 추천 코스 목록을 조회하는 메서드
	 * 
	 * @param userLocation 사용자 주소 중 시,군,구
	 * @param processedDistrict 
	 * @param cardCount    표시할 카드 개수
	 * @return 주소기반 추천코스 리스트
	 */
	public List<CourseCardDTO> getRecommendedCourses(String county, String district, int cardCount) {
		List<CourseCardDTO> list = new ArrayList<CourseCardDTO>();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			System.out.println("[DEBUG] CourseDAO.getRecommendedCourses() county: " + county + ", district: " + district);
			
			//String sql = "SELECT * FROM (SELECT v.* FROM vwCourseCards v INNER JOIN tblSpot s ON v.courseSeq = s.courseSeq WHERE s.place LIKE ? GROUP BY v.courseSeq, v.courseName, v.totalDistance, v.favoriteCount, v.curator, v.accountId ORDER BY v.favoriteCount DESC) WHERE ROWNUM <= ?";
			String sql = "SELECT * FROM (SELECT v.* FROM vwCourseCards v INNER JOIN tblSpot s ON v.courseSeq = s.courseSeq WHERE (s.place LIKE ? OR s.place LIKE ?) GROUP BY v.courseSeq, v.courseName, v.totalDistance, v.FAVORITECOUNT, v.curator, v.accountId ORDER BY v.FAVORITECOUNT DESC) WHERE ROWNUM <= ?";
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, "%" + county + "%");
	        pstat.setString(2, "%" + district + "%");
	        pstat.setInt(3, cardCount); // limit의 인덱스가 2에서 3으로 변경됨
			rs = pstat.executeQuery();

			while (rs.next()) {

				CourseCardDTO dto = new CourseCardDTO();

				dto.setCourseSeq(rs.getString("courseSeq"));
				dto.setCourseName(rs.getString("courseName"));
				dto.setTotalDistance(rs.getDouble("totalDistance"));
				dto.setFavoriteCount(rs.getInt("favoriteCount"));
				dto.setCurator(rs.getString("curator"));
				dto.setAccountId(rs.getString("accountId"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("CourseDAO.getRecommendedCourses failed");
			e.printStackTrace();
		} finally {
			// DB 자원을 반드시 반납합니다.
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstat != null)
					pstat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

<<<<<<< HEAD
	public CourseDetailDTO getCourseDetails(String courseSeq) {
		CourseDetailDTO courseDetail = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;

		try {
			// --- 1. 코스의 기본 정보 조회 (vwCourseCards 뷰 사용) ---
			String sql = "SELECT * FROM vwCourseCards WHERE courseSeq = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, courseSeq);
			rs = pstat.executeQuery();

			if (rs.next()) {
				courseDetail = new CourseDetailDTO();

				// DTO에 코스 기본 정보 설정
				courseDetail.setCourseSeq(rs.getString("courseSeq"));
				courseDetail.setCourseName(rs.getString("courseName"));
				courseDetail.setTotalDistance(rs.getDouble("totalDistance"));
				courseDetail.setFavoriteCount(rs.getInt("favoriteCount"));
				courseDetail.setCurator(rs.getString("curator"));
				courseDetail.setAccountId(rs.getString("accountId"));

				// --- 2. 해당 코스의 지점(Spot) 목록 조회 ---
				// 이전에 만든 getSpotsByCourseSeq 메서드를 재사용합니다.
				List<SpotDTO> spotList = getSpotsByCourseSeq(courseSeq);
				courseDetail.setSpots(spotList);

				// --- 3. (미래 확장) 해당 코스의 후기(Review) 목록 조회 ---
				// List<ReviewDTO> reviewList = getReviewsByCourseSeq(courseSeq);
				// courseDetail.setReviews(reviewList);
			}

		} catch (Exception e) {
			System.out.println("CourseDAO.getCourseDetails 실패");
			e.printStackTrace();
		} finally {
			// 자원 반납
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstat != null)
					pstat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return courseDetail; // 조회 실패 시 null 반환
	}

	public List<SpotDTO> getSpotsByCourseSeq(String courseSeq) {
		List<SpotDTO> list = new ArrayList<>();
		PreparedStatement pstat = null;
		ResultSet rs = null;

		try {
			// [핵심] spotStep 순서대로 정렬하여 경로 순서를 보장합니다.
			String sql = "SELECT * FROM tblSpot WHERE courseSeq = ? ORDER BY spotStep ASC";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, courseSeq);
			rs = pstat.executeQuery();

			while (rs.next()) {
				SpotDTO dto = new SpotDTO();

				// DB 조회 결과를 SpotDTO 객체에 매핑합니다.
				dto.setSpotSeq(rs.getString("spotSeq"));
				dto.setPlace(rs.getString("place")); // 사용자가 입력한 별명
				dto.setLat(rs.getString("lat"));
				dto.setLng(rs.getString("lng"));
				dto.setCourseSeq(rs.getString("courseSeq"));
				dto.setSpotStep(rs.getInt("spotStep"));

				list.add(dto);
			}

		} catch (Exception e) {
			System.out.println("CourseDAO.getSpotsByCourseSeq 실패");
			e.printStackTrace();
		} finally {
			// DB 자원을 반드시 반납합니다.
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstat != null)
					pstat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 페이징 처리를 위해 승인된 모든 코스의 총 개수를 반환합니다.
	 * 
	 * @return 승인된 코스의 총 개수
	 */
	public int getTotalCourseCount() {
		int count = 0;
		PreparedStatement pstat = null;
		ResultSet rs = null;

		try {
			// vwCourseCards 뷰에서 총 개수만 빠르게 세어옵니다.
			String sql = "SELECT COUNT(*) as cnt FROM vwCourseCards";
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();

			if (rs.next()) {
				count = rs.getInt("cnt");
			}
		} catch (Exception e) {
			System.out.println("CourseDAO.getTotalCourseCount 실패");
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
		return count;

	}

	/**
	 * 지정된 범위(페이지)에 해당하는 모든 코스 목록을 조회합니다.
	 * 
	 * @param start 시작 행 번호
	 * @param end   끝 행 번호
	 * @return 해당 페이지의 코스 카드 정보가 담긴 리스트
	 */
	public List<CourseCardDTO> getAllCourses(int start, int end) {
		List<CourseCardDTO> list = new ArrayList<>();
		PreparedStatement pstat = null;
		ResultSet rs = null;

		try {
			// [핵심 SQL] Oracle에서 페이징을 처리하는 ROWNUM 쿼리
			String sql = "SELECT * FROM (SELECT a.*, ROWNUM as rnum FROM (SELECT * FROM vwCourseCards ORDER BY courseSeq DESC) a) WHERE rnum BETWEEN ? AND ?";

			pstat = conn.prepareStatement(sql);
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			rs = pstat.executeQuery();

			while (rs.next()) {
				CourseCardDTO dto = new CourseCardDTO();
				// (DTO에 데이터 담는 로직은 getPopularCourses와 동일)
				dto.setCourseSeq(rs.getString("courseSeq"));
				dto.setCourseName(rs.getString("courseName"));
				dto.setTotalDistance(rs.getDouble("totalDistance"));
				dto.setFavoriteCount(rs.getInt("favoriteCount"));
				dto.setCurator(rs.getString("curator"));
				dto.setAccountId(rs.getString("accountId"));
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("CourseDAO.getAllCourses 실패");
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
		return list;
	}
=======

	
	
	
	
	
	
>>>>>>> parent of 1a8a729 (코스 상세보기 페이지, 코스 전체보기+검색 페이지 마무리)

}