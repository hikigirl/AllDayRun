<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
	<link rel="stylesheet" href="/alldayrun/asset/css/coursedetail.css" />
	<link rel="stylesheet" href="/alldayrun/asset/css/courseregister.css" />
</head>
<body>
	
	
	<div id="layout">
		<!-- 헤더 -->
  		<%@include file="/WEB-INF/views/inc/header.jsp"%>

 		 <!-- 사이드바 + 메인컨텐츠 -->
  		<div class="content-wrapper">
    		<!-- 메인 컨텐츠 영역 -->
    		<div class="main-content" id="admincourseDetail-main">
    		<!-- 여기 태그 내부에 본인 담당 페이지 html를 작성 -->
    			<div class="detail-section">
    				<h2 class="section-title">코스 경로</h2>
                    <div id="map-container">
                        <!-- 나중에 JavaScript가 이 곳에 지도를 그립니다. -->
                    </div>
                    
                    <%-- ✅ [추가] JavaScript로 데이터를 전달하기 위한 보이지 않는 div --%>
			        <div id="spot-data-container" data-spots='[
			            <c:forEach var="spot" items="${course.spots}" varStatus="loop">
			                {
			                    "place": "${spot.place}",
			                    "lat": ${spot.lat},
			                    "lng": ${spot.lng}
			                }
			                <c:if test="${not loop.last}">,</c:if>
			            </c:forEach>
			        ]'></div>
                    
    			</div>
    			
    			<div class="detail-section">
    				<div id="course-info-container">
                        <h1 id="course-title">${course.courseName}</h1>
                        <%-- ✅ [핵심] 지점 목록을 표시할 컨테이너 --%>
				        <div id="spot-list-container">
				            
				            <c:forEach var="spot" items="${course.spots}" varStatus="loop">
				                <div class="input-group" style="margin-bottom: 15px;">
				                    <%-- 라벨 동적 생성 (출발지/경유지/도착지) --%>
				                    <span style="font-weight: bold; width: 80px;">
				                        <c:choose>
				                            <c:when test="${loop.first}">출발지</c:when>
				                            <c:when test="${loop.last}">도착지</c:when>
				                            <c:otherwise>경유지${loop.count - 1}</c:otherwise>
				                        </c:choose>
				                    </span>
				                    
				                    <%-- 주소와 별명 표시 --%>
				                    <div class="spot-details">
				                        <div class="auto-address" id="address-${loop.index}">
				                            <%-- DB에 주소를 저장했다면 여기에 표시: ${spot.address} --%>
				                            <%-- 지금은 좌표로 대체 표시 (나중에 JS로 주소 변환 가능) --%>
				                            좌표를 주소로 변환
				                        </div>
				                        <div class="custom-name-input" > <!-- style="padding: 8px; border: 1px solid #ddd; border-top: none; border-radius: 0 0 4px 4px;" -->
				                            ${spot.place} <%-- 사용자가 입력한 별명 --%>
				                        </div>
				                    </div>
				                </div>
				            </c:forEach>
				        </div>
                        
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
    			
    		</div>
    		
    		<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	<%@include file="/WEB-INF/views/inc/courseasset.jsp"%>
	<script>
		
	</script>
</body>
</html>