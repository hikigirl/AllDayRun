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
 * 계정 관련 데이터베이스 작업을 처리하는 Data Access Object (DAO) 클래스
 * `tblAccountInfo` 및 `tblAccountInfoDetail` 테이블과의 상호작용을 담당한다.
 */
public class AccountDAO {

    // private DBUtil util;
    private Connection conn;
    private Statement stat;
    private PreparedStatement pstat;
    private ResultSet rs;

    /**
     * AccountDAO의 생성자
     * JNDI를 사용하여 데이터베이스 연결을 초기화한다.
     */
    public AccountDAO() {
        try {
            Context ctx = new InitialContext();
            Context env = (Context) ctx.lookup("java:comp/env");
            DataSource ds = (DataSource) env.lookup("jdbc/pool");

            conn = ds.getConnection();

            stat = conn.createStatement();
        } catch (Exception e) {
            System.out.println("Exception - AccountDAO.AccountDAO()");
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스 관련 자원(Connection, Statement, ResultSet)을 닫는다.
     */
    public void close() {
        // try { if (rs != null) rs.close(); } catch (Exception e) {}
        // try { if (pstat != null) pstat.close(); } catch (Exception e) {}
        // try { if (stat != null) stat.close(); } catch (Exception e) {}
        // try { if (conn != null) conn.close(); } catch (Exception e) {}
        try {
            this.conn.close();
        } catch (Exception e) {
            // handle exception
            System.out.println("CourseDAO.close()");
            e.printStackTrace();
        }
    }

    /**
     * 새로운 계정 정보를 `tblAccountInfo` 테이블에 삽입한다.
     *
     * @param dto 삽입할 계정 정보를 담고 있는 AccountDTO 객체
     * @return 계정 생성 성공 시 true, 실패 시 false를 반환
     */
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
            try {
                if (pstat != null)
                    pstat.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * 새로운 계정 상세 정보를 `tblAccountInfoDetail` 테이블에 삽입
     *
     * @param dto 삽입할 계정 상세 정보를 담고 있는 AccountDetailDTO 객체
     * @return 계정 상세 정보 생성 성공 시 true, 실패 시 false를 반환
     */
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
            pstat.setString(4, dto.getBirthday()); // 'YYYY-MM-DD' 또는 DB 포맷에 맞춰 전달
            pstat.setString(5, dto.getGender()); // 'male'/'female' 매핑 필요시 변환
            pstat.setString(6, dto.getRegionCity());
            pstat.setString(7, dto.getRegionCounty());
            pstat.setString(8, dto.getRegionDistrict());
            pstat.setString(9, dto.getExerciseFrequency());
            return pstat.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstat != null)
                    pstat.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * 주어진 계정 ID와 비밀번호로 로그인 유효성을 검증
     *
     * @param accountId 사용자 계정 ID(이메일)
     * @param password  사용자 비밀번호
     * @return 로그인 정보가 유효하면 true, 그렇지 않으면 false를 반환
     */
    public boolean validateLogin(String accountId, String password) {
        String sql = "select count(*) from tblAccountInfo where accountId = ? and password = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, accountId);
            pstat.setString(2, password);
            rs = pstat.executeQuery();
            if (rs.next())
                return rs.getInt(1) > 0;
        } catch (Exception e) {
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
        return false;
    }

    /**
     * 주어진 계정 ID가 이미 존재하는지 확인
     *
     * @param accountId 확인할 계정 ID(이메일)
     * @return 계정 ID가 존재하면 true, 그렇지 않으면 false를 반환
     */
    public boolean existsByAccountId(String accountId) {
        String sql = "select count(*) from tblAccountInfo where accountId = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, accountId);
            rs = pstat.executeQuery();
            if (rs.next())
                return rs.getInt(1) > 0;
        } catch (Exception e) {
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
        return false;
    }

    /**
     * 주어진 계정 ID에 해당하는 단일 계정 정보를 조회
     * AccountRole은 role로, AccountLevel은 level로 매핑된다.
     *
     * @param accountId 조회할 계정 ID(이메일)
     * @return 조회된 AccountDTO 객체이다. 해당하는 계정이 없으면 null을 반환
     */
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
                dto.setAccountRole(rs.getString("AccountRole")); // 매핑
                dto.setAccountSeq(rs.getString("AccountSeq"));
                dto.setAccountLevel(rs.getString("AccountLevel")); // 매핑
                dto.setRegisterType(rs.getString("registerType"));
                dto.setAccountCategory(rs.getString("accountCategory"));
                return dto;
            }
        } catch (Exception e) {
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
        return null;
    }

    /**
     * 주어진 계정 ID의 비밀번호를 업데이트(비밀번호 변경)
     *
     * @param accountId   비밀번호를 변경할 계정 ID(이메일)
     * @param newPassword 새로운 비밀번호
     * @return 비밀번호 업데이트 성공 시 true, 실패 시 false를 반환
     */
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
            try {
                if (pstat != null)
                    pstat.close();
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**
     * 카카오 로그인 사용자를 위한 계정을 생성하거나 업데이트한다 (upsert).
     * 계정이 존재하지 않으면 새로 생성하고, 이미 존재하면 아무 작업도 수행하지 않는다.
     *
     * @param email    카카오 사용자 이메일 (계정 ID로 사용된다).
     * @param nickname 카카오 사용자 닉네임이다.
     * @return 작업 성공 시 true, 실패 시 false를 반환한다.
     */
    public boolean upsertKakaoUser(String email, String nickname) {
        try {
            if (existsByAccountId(email))
                return true;
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
            try {
                if (pstat != null)
                    pstat.close();
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**
     * 주어진 계정 ID의 계정 역할을 조회한다.
     *
     * @param accountId 역할을 조회할 계정 ID(이메일)
     * @return 계정 역할 문자열이다. 해당하는 계정이 없으면 null을 반환
     */
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
        return role;
    }

}
