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
			<!-- 사이드바 일단은 생략, 꼭 주석 풀기 -->
			<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
			<!-- 메인 컨텐츠 영역 -->
			<div class="main-content" id="courseMain">
				<form id="searchBox" action="/alldayrun/course/coursemain.do" method="GET">
					<input type="text" id="searchKeyword" name="keyword"
						placeholder="지역, 코스 이름 등으로 검색해보세요." value="${keyword}"/>
					<button type="submit" id="searchButton">검색</button>
				</form>

				<div id="recommend-container">
					<div id="message">이런 코스는 어떠세요?</div>

					<div class="course-grid">
						<%-- ✅ [수정 1] 검색을 '시도'했고, 그 결과가 없을 때만 표시 --%>
					    <c:if test="${not empty keyword and empty courseList}">
					        <p><strong>'${keyword}'</strong>에 대한 검색 결과가 없습니다. 다른 키워드로 검색해보세요.</p>
					    </c:if>
					
					    <%-- ✅ [수정 2] 검색을 '시도하지 않았고'(초기 페이지), 추천 목록이 없을 때 표시 --%>
					    <c:if test="${empty keyword and empty courseList}">
					        <p>표시할 추천 코스가 없습니다.</p>
					    </c:if>
						<!-- 카드 1 -->
						<c:forEach var="course" items="${courseList}">
						<!-- a태그는 코스 클릭시 상세보기 페이지로 이동하기 위한 것, href 수정 필요 -->
						<a href="/alldayrun/course/detail.do?courseSeq=${course.courseSeq}" class="course-card-link">
						<div class="course-card">
							<div class="card-map">
								<img src="/alldayrun/asset/courseimg/card_map_sample.png"
									alt="코스 지도 예시">
							</div>
							<div class="card-details">
								<h3 class="course-title">${course.courseName}</h3>
								<div class="course-info">
									<span>거리</span> <span class="kilometers">${course.totalDistance}km</span>
								</div>
								<div class="course-info">
									<span>즐겨찾기</span> <span>${course.favoriteCount}</span>
								</div>
								<div class="course-curator">${course.curator}</div>
							</div>
						</div>
						</a>
						</c:forEach>

						<!-- 카드 2 -->
						<!-- <div class="course-card">
							<div class="card-map">
								<img src="/alldayrun/asset/courseimg/card_map_sample.png"
									alt="코스 지도 예시">
							</div>
							<div class="card-details">
								<h3 class="course-title">하드코딩한 카드입니다</h3>
								<div class="course-info">
									<span>거리</span> <span class="kilometers">2.2km</span>
								</div>
								<div class="course-info">
									<span>즐겨찾기</span> <span>3</span>
								</div>
								<div class="course-curator">러닝 마스터 AllDayRunners</div>
							</div>
						</div> -->


					</div>
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