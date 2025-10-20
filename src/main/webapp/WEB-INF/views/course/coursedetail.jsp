<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
	<link rel="stylesheet" href="/alldayrun/asset/css/coursedetail.css" />
</head>
<body>
	
	<div id="layout">
		<!-- 헤더 -->
  		<%@include file="/WEB-INF/views/inc/header.jsp"%>

 		 <!-- 사이드바 + 메인컨텐츠 -->
  		<div class="content-wrapper">
    		<!-- 메인 컨텐츠 영역 -->
    		<div class="main-content" id="courseDetail-main">
    			<div class="detail-section">
    				<h2 class="section-title">코스 경로</h2>
                    <div id="map-container">
                        <!-- 나중에 JavaScript가 이 곳에 지도를 그립니다. -->
                    </div>
    			</div>
    			
    			<div class="detail-section">
    				<div id="course-info-container">
                        <h1 id="course-title">${course.courseName}</h1>
                        <div class="info-item">
                            <span class="label">총 거리</span>
                            <span class="value">${course.totalDistance} km</span>
                        </div>
                        <div class="info-item">
                            <span class="label">등록자</span>
                            <span class="value">${course.curator}</span>
                        </div>
                        
                        <div id="action-buttons">
                            <%-- 로그인 상태일 때만 즐겨찾기 버튼 표시 --%>
                            <c:if test="${not empty sessionScope.accountId}">
                                <button type="button" class="action-btn" id="btn-favorite">즐겨찾기 추가</button>
                            </c:if>

                            <%-- 본인이 등록한 코스일 때만 삭제 요청 버튼 표시 --%>
                            <c:if test="${sessionScope.accountId == course.accountId}">
                                <button type="button" class="action-btn" id="btn-delete">삭제하기</button>
                            </c:if>
                        </div>
                    </div>
    			
    			</div>
    			
    			<hr id="coursedetail-hr" />
    			
    			<div class="detail-section">
    				<h2 class="section-title">코스 후기 (${fn:length(reviewList)})</h2>
                    <div id="review-container">
                        <%-- c:forEach를 사용해 댓글 목록을 반복 --%>
                        <c:forEach var="review" items="${reviewList}">
                            <div class="review-item">
                                <div class="review-header">
                                    <span class="review-author">${review.authorNickname}</span>
                                    <span class="review-date">${review.regDate}</span>
                                </div>
                                <p class="review-content">${review.content}</p>
                            </div>
                        </c:forEach>
                        
                        <c:if test="${empty reviewList}">
                            <p>아직 등록된 후기가 없습니다. 첫 후기를 남겨주세요!</p>
                        </c:if>
                    </div>
                    <%-- TODO: 댓글 작성 폼 추가 --%>
    			</div>
    			
    			
    			
    		</div>
    		
    		<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
		
	</script>
</body>
</html>