
package com.test.run.user;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpSession;

import com.test.run.user.model.AccountDTO;
import com.test.run.user.model.AccountDetailDTO;
import com.test.run.user.model.AccountDAO;

@WebServlet("/user/registerstep2.do")
@MultipartConfig(maxFileSize = 5_000_000) // 5MB
public class RegisterStep2 extends HttpServlet {

    private static final Set<String> TAKEN = new HashSet<>(Arrays.asList("runner01","올데이런7","user1234"));
    private static final Pattern NAME_KO = Pattern.compile("^[가-힣]{1,8}$");
    private static final Pattern NAME_EN = Pattern.compile("^[A-Za-z]{1,8}$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/user/registerstep2.jsp");
        rd.forward(req, resp);
    }
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        System.out.println(">>> action = " + action);

        if ("checkNick".equals(action)) {
            String nick = req.getParameter("nickname");
            boolean dup = nick != null && TAKEN.contains(nick);
            resp.setContentType("application/json;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                out.printf("{\"duplicate\":%s}", dup ? "true":"false");
            }
            return;
        }

        if ("submit".equals(action)) {

            // Form Parameters
            String lastName  = req.getParameter("lastName");
            String firstName = req.getParameter("firstName");
            String nickname = req.getParameter("nickname");
            String phoneTail = req.getParameter("phoneTail");
            String gender = req.getParameter("gender");
            String yyyy = req.getParameter("yyyy");
            String mm = req.getParameter("mm");
            String dd = req.getParameter("dd");
            String regionCity = req.getParameter("regionCity");
            String regionCounty = req.getParameter("regionCounty");
            String regionDistrict = req.getParameter("regionDistrict");
            String freq = req.getParameter("freq");
            String[] goals = req.getParameterValues("goals");

            // 0) 이름 유효성 검사
            boolean bothKor = lastName != null && firstName != null
                    && lastName.matches("^[가-힣]{1,8}$")
                    && firstName.matches("^[가-힣]{1,8}$");
            boolean bothEng = lastName != null && firstName != null
                    && lastName.matches("^[A-Za-z]{1,8}$")
                    && firstName.matches("^[A-Za-z]{1,8}$");
            boolean nameOk = bothKor || bothEng;
            String fullName = nameOk
                    ? (bothKor ? (lastName + firstName) : (lastName + " " + firstName))
                    : "";

            // 1) 프로필 사진
            Part photo = req.getPart("photo");
            String profilePhoto = "pic.png";
            if (photo != null && photo.getSize() > 0) {
                String ext = getExt(photo);
                if (!ext.matches("(?i)png|jpg|jpeg")) {
                    req.setAttribute("photoMsg", "png, jpg, jpeg만 업로드 가능합니다.");
                    forward(req, resp); return;
                }
                String uploadDir = req.getServletContext().getRealPath("/upload/profile");
                new File(uploadDir).mkdirs();
                profilePhoto = "p_" + System.currentTimeMillis() + "." + ext.toLowerCase();
                photo.write(uploadDir + File.separator + profilePhoto);
            }

            // 유효성 검사
            boolean nickOk = nickname != null && nickname.matches("^[A-Za-z0-9가-힣]{1,12}$") && !TAKEN.contains(nickname);
            boolean phoneOk = phoneTail != null && phoneTail.matches("^[0-9]{8}$");
            boolean genderOk = "남자".equals(gender) || "여자".equals(gender);
            boolean birthOk = yyyy!=null && mm!=null && dd!=null
                    && yyyy.matches("^[0-9]{4}$") && mm.matches("^(0?[1-9]|1[0-2])$")
                    && dd.matches("^([0]?[1-9]|[12][0-9]|3[01])$");

            boolean regionCityOk = regionCity != null && !regionCity.isEmpty();
            boolean regionCountyOk = regionCounty != null && regionCounty.matches("^[가-힣0-9\s-]{1,25}$");
            boolean regionDistrictOk = regionDistrict != null && regionDistrict.matches("^[가-힣0-9\s-]{1,25}$");
            boolean regionOk = regionCityOk && regionCountyOk && regionDistrictOk;

            if (!regionCityOk)     req.setAttribute("regionCityMsg", "시·도를 선택하세요.");
            if (!regionCountyOk)   req.setAttribute("regionCountyMsg", "시군/구 형식을 확인하세요.");
            if (!regionDistrictOk) req.setAttribute("regionDistrictMsg", "동/읍 형식을 확인하세요.");

            boolean freqOk = freq != null && (freq.equals("1-3") || freq.equals("4-5") || freq.equals("6+"));
            boolean goalsOk = goals != null && goals.length >= 1;
            System.out.println("[DEBUG] goals.length" + goals.length);
            System.out.println("nickOk=" + nickOk + ", phoneOk=" + phoneOk + ", genderOk=" + genderOk
            	    + ", birthOk=" + birthOk + ", regionOk=" + regionOk + ", freqOk=" + freqOk + ", goalsOk=" + goalsOk);

            // 이름 유효성 실패 시 즉시 복원 후 리턴
            if (!nameOk) {
                req.setAttribute("nameMsg", "사용할 수 없는 이름입니다.");
                req.setAttribute("photoName", profilePhoto);
                req.setAttribute("firstName", firstName);
                req.setAttribute("lastName", lastName);
                req.setAttribute("nickname", nickname);
                req.setAttribute("phoneTail", phoneTail);
                req.setAttribute("gender", gender);
                req.setAttribute("yyyy", yyyy);
                req.setAttribute("mm", mm);
                req.setAttribute("dd", dd);
                req.setAttribute("regionCity", regionCity);
                req.setAttribute("regionCounty", regionCounty);
                req.setAttribute("regionDistrict", regionDistrict);
                req.setAttribute("freq", freq);
                req.setAttribute("goals", goals);
                forward(req, resp);
                return;
            }

            if (!(nickOk && phoneOk && genderOk && birthOk && regionOk && freqOk && goalsOk)) {
                req.setAttribute("photoName", profilePhoto);
                req.setAttribute("firstName", firstName);
                req.setAttribute("lastName", lastName);
                req.setAttribute("nickname", nickname);
                req.setAttribute("phoneTail", phoneTail);
                req.setAttribute("gender", gender);
                req.setAttribute("yyyy", yyyy);
                req.setAttribute("mm", mm);
                req.setAttribute("dd", dd);
                req.setAttribute("regionCity", regionCity);
                req.setAttribute("regionCounty", regionCounty);
                req.setAttribute("regionDistrict", regionDistrict);
                req.setAttribute("freq", freq);
                req.setAttribute("goals", goals);
                forward(req, resp);
                return;
            }

            HttpSession session = req.getSession();

            AccountDTO a = new AccountDTO();
            a.setAccountId((String)session.getAttribute("reg_accountId"));
            a.setPassword((String)session.getAttribute("reg_password"));
            a.setProfilePhoto(profilePhoto);
            a.setNickname(nickname);
            a.setRegisterType("기본");

            AccountDetailDTO d = new AccountDetailDTO();
            d.setAccountId((String) session.getAttribute("reg_accountId"));
            d.setName(fullName);
            String phoneFormatted = String.format("010-%s-%s", phoneTail.substring(0, 4), phoneTail.substring(4));
            d.setPhoneNum(phoneFormatted);
            d.setBirthday(String.format("%s-%02d-%02d", yyyy, Integer.parseInt(mm), Integer.parseInt(dd)));
            d.setGender(gender);
            d.setRegionCity(regionCity);
            d.setRegionCounty(regionCounty);
            d.setRegionDistrict(regionDistrict);
            d.setExerciseFrequency(freq);
            
            System.out.println(">>> accountId = " + session.getAttribute("reg_accountId"));
            System.out.println(">>> password = " + session.getAttribute("reg_password"));


            AccountDAO dao = new AccountDAO();
            try {
                boolean ok1 = dao.createAccount(a);
                if (!ok1) { req.setAttribute("saveMsg", "계정 생성 실패"); forward(req, resp); return; }
                boolean ok2 = dao.createAccountDetail(d);
                if (!ok2) { req.setAttribute("saveMsg", "상세 정보 저장 실패"); forward(req, resp); return; }
            } finally {
                dao.close();
            }

            System.out.println("==> 성공 진입 가능, success JSP로 forward 직전");

            System.out.println("==> 성공 직전: jsp forward 준비");
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/user/registersuccess.jsp");
            if (rd == null) {
                System.out.println("🚨 Dispatcher is null!");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JSP 경로 문제");
                return;
            }
            System.out.println("==> Dispatcher 준비됨, forward 실행");
            rd.forward(req, resp);
            System.out.println("==> forward 이후");

            return;
        }

        System.out.println("==> default forward to step2.jsp 실행됨");

        forward(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/user/registerstep2.jsp");
        rd.forward(req, resp);
    }

    private String getExt(Part part) throws IOException {
        String submitted = part.getSubmittedFileName();
        if (submitted == null) return "";
        int idx = submitted.lastIndexOf('.');
        String ext = (idx > -1) ? submitted.substring(idx + 1) : "";
        if (ext.isEmpty()) {
            String mime = Files.probeContentType(new File(submitted).toPath());
            if (mime != null && mime.contains("/")) ext = mime.substring(mime.indexOf('/') + 1);
        }
        return ext;
    }
}
