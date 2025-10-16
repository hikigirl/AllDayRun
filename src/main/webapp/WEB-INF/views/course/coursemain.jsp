<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<link rel="stylesheet" href="/alldayrun/asset/css/coursemain.css" />
</head>
<body>

	<div id="layout">
		<!-- 헤더 -->
		<%@include file="/WEB-INF/views/inc/header.jsp"%>

		<!-- 사이드바 + 메인 -->
		<div class="content-wrapper">
			<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
			
			<!-- 메인 컨텐츠 영역 -->
			<div class="main-content" id="courseMain">
			
				<!-- 검색창 -->
				<form id="searchBox" action="/alldayrun/course/coursemain.do" method="GET">
					<input type="text" id="searchKeyword" name="keyword"
						placeholder="지역, 코스 이름 등으로 검색해보세요." value="${keyword}"/>
					<button type="submit" id="searchButton">검색</button>
				</form>

				<!-- 추천 내부에서 시나리오 분기 -->
				<div id="recommend-container">
				
				<!-- 
				======================================================
				1. 검색 결과 표시(searchList가 있을 때에만 보임)
				======================================================				-->
				
				<c:if test="${not empty searchList or not empty keyword}">
					<div id="search-message" class="message plain"><span>'${keyword}'</span>에 대한 검색 결과입니다.</div>
					<div class="course-grid">
						<!-- [1] 검색 결과가 없을 때 표시 -->
					    <c:if test="${empty searchList}">
					        <div id="search-message-none" class="message plain"><span>'${keyword}'</span>에 대한 검색 결과가 없습니다. 다른 키워드로 검색해보세요.</div>
					    </c:if>
					
					    
						<!-- 카드 1 -->
						<c:forEach var="course" items="${searchList}">
						
						<!-- a태그는 코스 클릭시 상세보기 페이지로 이동하기 위한 것, href 수정 필요 -->
						<a href="/alldayrun/course/coursedetail.do?courseSeq=${course.courseSeq}" class="course-card-link">
						<div class="course-card">
							<div class="card-map">
								<img src="/alldayrun/asset/courseimg/card_map_sample.png"
									alt="코스 지도 예시">
							</div>
							<div class="card-details">
								<h3 class="course-title">${course.courseName}</h3>
								<div class="course-info">
									<span>직선거리</span>&nbsp;&nbsp;<span class="kilometers">${course.totalDistance}km</span>
								</div>
								<div class="course-info">
									<span>즐겨찾기</span>&nbsp;&nbsp;<span>${course.favoriteCount}</span>
								</div>
								<div class="course-curator">
									<span>등록</span>&nbsp;<span>${course.curator}</span>
								</div>
							</div>
						</div>
						</a>
						
						</c:forEach>
					</div>
				</c:if>	
					
				<!-- 
				======================================================
				2. 일반 페이지 로딩 (검색어가 없을 때만 보임)
				======================================================				-->	
				<c:if test="${empty keyword}">

					<!-- 인기 코스 목록 (로그인/비로그인 공통) -->
        			<c:if test="${not empty popularList}">
						<div id="message-hotcourse" class="message title">🔥지금 가장 인기있는 코스</div>
						<div class="course-grid">
							<c:forEach var="course" items="${popularList}">
							
							<!-- a태그는 코스 클릭시 상세보기 페이지로 이동하기 위한 것, href 수정 필요 -->
							<a href="/alldayrun/course/coursedetail.do?courseSeq=${course.courseSeq}" class="course-card-link">
							<div class="course-card">
								<div class="card-map">
									<img src="/alldayrun/asset/courseimg/card_map_sample.png"
										alt="코스 지도 예시">
								</div>
								<div class="card-details">
									<h3 class="course-title">${course.courseName}</h3>
									<div class="course-info">
										<span>직선거리</span>&nbsp;&nbsp;<span class="kilometers">${course.totalDistance}km</span>
									</div>
									<div class="course-info">
										<span>즐겨찾기</span>&nbsp;&nbsp;<span>${course.favoriteCount}</span>
									</div>
									<div class="course-curator">
										<span>등록</span>&nbsp;<span>${course.curator}</span>
									</div>
								</div>
							</div>
							</a>
							
							</c:forEach>
						</div>
					</c:if> <!-- c:if test="${not empty popularList}" -->
					
					<!-- 추천 코스 목록 (로그인 사용자 전용) -->
        			<c:if test="${not empty sessionScope.loginUserEmail and not empty recommendedList}">
					<div id="message" class="message title">📍회원님을 위한 지역 기반 추천 코스</div>
					<div class="course-grid">
						<c:forEach var="course" items="${recommendedList}">
						
						<!-- a태그는 코스 클릭시 상세보기 페이지로 이동하기 위한 것, href 수정 필요 -->
						<a href="/alldayrun/course/coursedetail.do?courseSeq=${course.courseSeq}" class="course-card-link">
						<div class="course-card">
							<div class="card-map">
								<img src="/alldayrun/asset/courseimg/card_map_sample.png"
									alt="코스 지도 예시">
							</div>
							<div class="card-details">
								<h3 class="course-title">${course.courseName}</h3>
								<div class="course-info">
									<span>직선거리</span>&nbsp;&nbsp;<span class="kilometers">${course.totalDistance}km</span>
								</div>
								<div class="course-info">
									<span>즐겨찾기</span>&nbsp;&nbsp;<span>${course.favoriteCount}</span>
								</div>
								<div class="course-curator">
									<span>등록</span>&nbsp;<span>${course.curator}</span>
								</div>
							</div>
						</div>
						</a>
						
						</c:forEach>
					</div>
					</c:if> <!-- c:if test="${not empty sessionScope.loginUserEmail and not empty recommendedList}" -->
					<!-- 보여줄 코스가 하나도 없을 때 -->
					<c:if test="${empty popularList and empty recommendedList}">
						<div id="course-empty" class="message plain">표시할 코스가 없습니다.</div>
					</c:if>
				</c:if>	<!-- c:if ${empty keyword} -->
				</div>
			</div>

		</div>
	</div>


	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	<script
		src="${pageContext.request.contextPath}/asset/js/coursemain.js"></script>
	<script>
		
	</script>
</body>
</html>