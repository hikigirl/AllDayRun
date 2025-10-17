<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
   <style>
/* main-content 안에서만 적용 */
.main-content {
    width: 800px;
    margin: 40px auto;
    padding: 30px;
    background: #fff;
    border-radius: 18px;
    box-shadow: 0 8px 25px rgba(0,0,0,0.08);
    animation: fadeIn 0.5s ease-in-out;
    font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
    color: #333;
}

/* 폼 컨테이너 */
.main-content .form-container {
    width: 100%;
    padding: 20px;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.05);
    background: #f7f9ff;
}

/* 폼 그룹 */
.main-content .form-group {
    margin-bottom: 18px;
}

.main-content .form-group label {
    display: block;
    margin-bottom: 8px;
    font-weight: 600;
    font-size: 14px;
    color: #1f2a56;
}

.main-content .form-group input[type="text"],
.main-content .form-group textarea,
.main-content .form-group input[type="file"] {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #cbd5e1;
    border-radius: 8px;
    font-size: 14px;
    box-sizing: border-box;
}

.main-content .form-group textarea {
    min-height: 250px;
    resize: vertical;
}

.main-content .form-group .current-file {
    font-size: 13px;
    color: #555;
    margin-bottom: 6px;
}

/* 안내 문구 */
.main-content .form-group p {
    font-size: 12px;
    color: #888;
    margin-top: 4px;
}

/* 버튼 컨테이너 */
.main-content .form-buttons {
    text-align: right;
    margin-top: 20px;
}

/* 버튼 스타일 */
.main-content .form-buttons button {
    padding: 10px 22px;
    margin-left: 8px;
    border-radius: 10px;
    border: none;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
}

.main-content .form-buttons button[type="submit"] {
    background-color: #4f6ef7;
    color: #fff;
}

.main-content .form-buttons button[type="submit"]:hover {
    background-color: #3554db;
    box-shadow: 0 4px 12px rgba(79,110,247,0.3);
    transform: translateY(-2px);
}

.main-content .form-buttons button[type="button"] {
    background-color: #e2e8f0;
    color: #333;
}

.main-content .form-buttons button[type="button"]:hover {
    background-color: #cbd5e1;
}

/* 애니메이션 */
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
}

/* 반응형 */
@media (max-width: 900px) {
    .main-content {
        width: 95%;
        padding: 20px;
    }
    .main-content .form-buttons {
        text-align: center;
    }
    .main-content .form-buttons button {
        width: 45%;
        margin: 5px 2%;
    }
}
</style>
</head>
<body>
	
	<div id="layout">
		<!-- 헤더 -->
  		<%@include file="/WEB-INF/views/inc/header.jsp"%>

 		 <!-- 사이드바 + 메인 -->
  		<div class="content-wrapper">
    		<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
    		<!-- 메인 컨텐츠 영역 -->
    		<div class="main-content" id="">
    			<div class="form-container">
                <form method="POST" action="/alldayrun/crewboardeditok.do" enctype="multipart/form-data">
                    
                    <input type="hidden" name="boardContentSeq" value="${dto.boardContentSeq}">
                    <input type="hidden" name="oldAttach" value="${dto.attach}">

                    <div class="form-group">
                        <label for="title">제목</label>
                        <input type="text" id="title" name="title" required value="${dto.title}">
                    </div>
                    <div class="form-group">
                        <label for="content">내용</label>
                        <textarea id="content" name="content" required>${dto.content}</textarea>
                    </div>
                    <div class="form-group">
                        <label for="attach">파일 첨부</label>
                        <div class="current-file">현재 파일: ${not empty dto.attach ? dto.attach : '없음'}</div>
                        <input type="file" id="attach" name="attach">
                        <p style="font-size:12px; color:#888;">새 파일을 첨부하면 기존 파일은 삭제됩니다. 파일을 변경하지 않으려면 아무것도 첨부하지 마세요.</p>
                    </div>
                    <div class="form-buttons">
                        <button type="submit">수정 완료</button>
                        <button type="button" onclick="history.back();">취소</button>
                    </div>
                </form>
            </div>
    		</div>
    		
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
		
	</script>
</body>
</html>