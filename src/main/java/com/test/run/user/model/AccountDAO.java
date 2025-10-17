package com.test.run.user.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

//import com.test.util.DBUtil;

/**
 * 단일 DAO 파일
 * - 패키지: com.project.alldayrun.user  (model 패키지 아님)
 * - 테이블: tblAccountInfo
 * - 컬럼 매핑: AccountRole -> role, AccountLevel -> level
 */
public class AccountDAO {

    //private DBUtil util;
    private Connection conn;
    private Statement stat;
    private PreparedStatement pstat;
    private ResultSet rs;

    public AccountDAO() {
        try {
        	Context ctx = new InitialContext();
			Context env = (Context)ctx.lookup("java:comp/env");
			DataSource ds = (DataSource)env.lookup("jdbc/pool");
			
			conn = ds.getConnection();
			
            stat = conn.createStatement();
        } catch (Exception e) {
        	System.out.println("Exception - AccountDAO.AccountDAO()");
            e.printStackTrace();
        }
    }

    public void close() {
//        try { if (rs != null) rs.close(); } catch (Exception e) {}
//        try { if (pstat != null) pstat.close(); } catch (Exception e) {}
//        try { if (stat != null) stat.close(); } catch (Exception e) {}
//        try { if (conn != null) conn.close(); } catch (Exception e) {}
    	try {
			this.conn.close();
		} catch (Exception e) {
			// handle exception
			System.out.println("CourseDAO.close()");
			e.printStackTrace();
		}
    }
    
 // 계정 생성 (tblAccountInfo)
    public boolean createAccount(AccountDTO dto) {
        String sql = "insert into tblAccountInfo "
                   + "(accountId, password, profilePhoto, nickname, AccountRole, AccountSeq, AccountLevel, registerType, accountCategory) "
                   + "values (?, ?, ?, ?, default, seqAccountInfo.nextVal, default, ?, default)";

        PreparedStatement pstat = null;
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, dto.getAccountId());
            pstat.setString(2, dto.getPassword());
            pstat.setString(3, dto.getProfilePhoto() != null ? dto.getProfilePhoto() : "pic.png");
            pstat.setString(4, dto.getNickname());
            
            String regType = dto.getRegisterType();
            if (!"기본".equals(regType) && !"소셜".equals(regType)) {
                throw new IllegalArgumentException("유효하지 않은 registerType 값입니다: " + regType);
            }
            pstat.setString(5, regType);

            return pstat.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (pstat != null) pstat.close(); } catch (Exception ignore) {}
        }
    }

    // 상세정보 생성 (tblAccountInfoDetail)
    public boolean createAccountDetail(AccountDetailDTO dto) {
        String sql = "insert into tblAccountInfoDetail "
                   + "(accountInfoDetailSeq, accountId, name, phoneNum, birthday, gender, regionCity, regionCounty, regionDistrict, exerciseFrequency, joinDate) "
                   + "values (seqAccountInfoDetail.nextVal, ?, ?, ?, ?, ?, ?, ?, ?, ?, default)";

        PreparedStatement pstat = null;
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, dto.getAccountId());
            pstat.setString(2, dto.getName());
            pstat.setString(3, dto.getPhoneNum());
            pstat.setString(4, dto.getBirthday());       // 'YYYY-MM-DD' 또는 DB 포맷에 맞춰 전달
            pstat.setString(5, dto.getGender());         // 'male'/'female' 매핑 필요시 변환
            pstat.setString(6, dto.getRegionCity());
            pstat.setString(7, dto.getRegionCounty());
            pstat.setString(8, dto.getRegionDistrict());
            pstat.setString(9, dto.getExerciseFrequency());
            return pstat.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (pstat != null) pstat.close(); } catch (Exception ignore) {}
        }
    }

    /** 로그인 검증 */
    public boolean validateLogin(String accountId, String password) {
        String sql = "select count(*) from tblAccountInfo where accountId = ? and password = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, accountId);
            pstat.setString(2, password);
            rs = pstat.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstat != null) pstat.close(); } catch (Exception e) {}
        }
        return false;
    }

    /** 아이디 존재 여부 */
    public boolean existsByAccountId(String accountId) {
        String sql = "select count(*) from tblAccountInfo where accountId = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, accountId);
            rs = pstat.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstat != null) pstat.close(); } catch (Exception e) {}
        }
        return false;
    }

    /** 단일 계정 조회 (DTO 매핑: AccountRole->role, AccountLevel->level) */
    public AccountDTO findAccount(String accountId) {
        String sql = "select accountId, password, profilePhoto, nickname, AccountRole, AccountSeq, AccountLevel, registerType, accountCategory "
                   + "from tblAccountInfo where accountId = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, accountId);
            rs = pstat.executeQuery();
            if (rs.next()) {
                AccountDTO dto = new AccountDTO();
                dto.setAccountId(rs.getString("accountId"));
                dto.setPassword(rs.getString("password"));
                dto.setProfilePhoto(rs.getString("profilePhoto"));
                dto.setNickname(rs.getString("nickname"));
                dto.setAccountRole(rs.getString("AccountRole"));     // 매핑
                dto.setAccountSeq(rs.getString("AccountSeq"));
                dto.setAccountLevel(rs.getString("AccountLevel"));   // 매핑
                dto.setRegisterType(rs.getString("registerType"));
                dto.setAccountCategory(rs.getString("accountCategory"));
                return dto;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstat != null) pstat.close(); } catch (Exception e) {}
        }
        return null;
    }

    /** 비밀번호 변경 */
    public boolean updatePassword(String accountId, String newPassword) {
        String sql = "update tblAccountInfo set password = ? where accountId = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, newPassword);
            pstat.setString(2, accountId);
            return pstat.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (pstat != null) pstat.close(); } catch (Exception e) {}
        }
        return false;
    }

    /** 카카오 최초 로그인 시 자동 가입(upsert) */
    public boolean upsertKakaoUser(String email, String nickname) {
        try {
            if (existsByAccountId(email)) return true;
            String sql = "insert into tblAccountInfo "
                       + "(accountId, password, profilePhoto, nickname, AccountRole, AccountSeq, AccountLevel, registerType, accountCategory) "
                       + "values (?, 'kakao', 'pic.png', ?, '일반', seqAccountInfo.nextVal, 1, 2, 1)";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, email);
            pstat.setString(2, nickname != null ? nickname : "카카오유저");
            return pstat.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (pstat != null) pstat.close(); } catch (Exception e) {}
        }
        return false;
    }
    
    /** 계정 역할(Role) 조회 */
    public String getAccountRole(String accountId) {
        String sql = "select AccountRole from tblAccountInfo where accountId = ?";
        String role = null;
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, accountId);
            rs = pstat.executeQuery();
            if (rs.next()) {
                role = rs.getString("AccountRole");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstat != null) pstat.close(); } catch (Exception e) {}
        }
        return role;
    }

}
