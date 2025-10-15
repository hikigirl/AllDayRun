<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<%@include file="/WEB-INF/views/inc/courseasset_css.jsp"%>

</head>
<body>
	<div id="layout">
		<%@include file="/WEB-INF/views/inc/header.jsp"%>

		<div class="content-wrapper">
		<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
			<div id="main-content">
				<h2>코스 등록하기</h2>
				<p id="guide-message">입력란을 클릭하여 활성화한 후, 지도에서 원하는 위치를 클릭하세요.</p>

				<div class="course-editor">

					<div class="input-section">
						<!-- 코스명 입력칸 여기로 -->
						<div>
							<h3>코스 정보</h3>
							<div class="input-group">
								<span>코스명</span>
								<div class="spot-details">
									<input type="text" id="courseName" name="courseName"
										placeholder="코스의 이름을 입력하세요." maxlength="25">
									<!-- 코스명 - 최대 25자 -->
								</div>
							</div>
						</div>
						<hr id="input-section-hr">
						<div id="spot-groups-wrapper">
							<div class="input-group" data-index="0">
								<span>출발지</span>
								<div class="spot-details">
									<div class="auto-address">지도에서 장소를 선택하세요.</div>
									<input type="text" class="custom-name-input"
										placeholder="장소의 별명을 입력하세요." maxlength="25">
									<!-- 별명 - 최대 25자 -->
								</div>
							</div>

							<!-- <div id="waypoints-container"></div> -->

							<div class="input-group" id="end-point-group" data-index="1">
								<span>도착지</span>
								<div class="spot-details">
									<div class="auto-address">지도에서 장소를 선택하세요.</div>
									<input type="text" class="custom-name-input"
										placeholder="장소의 별명을 입력하세요." maxlength="25">
								</div>
								<button type="button" class="btn-action btn-add"
									id="add-waypoint-btn" title="경유지 추가"><img src="${pageContext.request.contextPath}/asset/buttonimg/add_btn.png" alt="경유지 추가"></button>
							</div>
						</div>

						<input type="button" value="이 코스 등록 요청하기" id="btnRegisterCourse">
					</div>

					<div id="map"></div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	<%@include file="/WEB-INF/views/inc/courseasset.jsp"%>
	<%-- <script src="${pageContext.request.contextPath}/asset/js/main.js"></script> --%>
	
	<script>
	    // JSP의 contextPath를 JavaScript 전역 변수로 저장합니다.
	    const contextPath = "${pageContext.request.contextPath}";
	</script>
	<script
		src="${pageContext.request.contextPath}/asset/js/courseregister.js"></script>
</body>
</html>