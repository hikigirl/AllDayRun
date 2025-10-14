<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/views/inc/asset.jsp"%>

</head>
<body>
	<%@include file="/WEB-INF/views/inc/header.jsp"%>
	<!-- 
	<div>코스 등록하기</div>
	<div>지도를 클릭하면 마커가 생성됩니다. 장소명을 입력하고, 지점 추가 버튼을 누르세요.</div>
	<div>코스명: <input type="text" id="courseName" name="courseName" placeholder="코스의 이름을 입력하세요."/></div>
	<div id="map"></div>
	<div>
		장소명: <input type="text" id="spotName" name="spotName" placeholder="지점의 이름을 입력하세요." />
		<input type="button" value="지점 추가하기" id="btnAdd" />
	</div>
	
	<div><input type="button" value="코스 신청하기" id="btnRegister" /></div>
	<div id="clickLatlng"></div>
	<div>출발지: <input type="text" name="placeStart" id="placeStart" placeholder="출발지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via1" id="via1" placeholder="경유지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via2" id="via2" placeholder="경유지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via3" id="via3" placeholder="경유지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via4" id="via4" placeholder="경유지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via5" id="via5" placeholder="경유지를 입력하세요."/></div>
	<div>도착지: <input type="text" name="placeEnd" id="placeEnd" placeholder="도착지를 입력하세요."/><span id="addVia">&nbsp;+&nbsp;</span></div>
	-->
	<!-- <input type="hidden" name="lat" id="lat">
	<input type="hidden" name="lng" id="lng">
	<input type="hidden" name="place" id="place"/> -->


	
	
	
	<div id="main-content">
	<h2>코스 등록하기</h2>
	<p id="guide-message">입력란을 클릭하여 활성화한 후, 지도에서 원하는 위치를 클릭하세요.</p>
	
	<div class="course-editor">

		<div class="input-section">
			<!-- 코스명 입력칸 여기로 -->
			<div>
				<h3>코스 정보</h3>
				<div class="input-group">
					<span>코스명</span>  <div class="spot-details"><input type="text" id="courseName" name="courseName" placeholder="코스의 이름을 입력하세요."></div>
				</div>
			</div>
			<hr id="input-section-hr">
			<div id="spot-groups-wrapper">
			<div class="input-group" data-index="0">
				<span>출발지</span>
				<div class="spot-details">
					<div class="auto-address">지도에서 장소를 선택하세요.</div>
					<input type="text" class="custom-name-input" placeholder="장소의 별명을 입력하세요.">
				</div>
			</div>

			<!-- <div id="waypoints-container"></div> -->

			<div class="input-group" id="end-point-group" data-index="1">
				<span>도착지</span>
				<div class="spot-details">
					<div class="auto-address">지도에서 장소를 선택하세요.</div>
					<input type="text" class="custom-name-input" placeholder="장소의 별명을 입력하세요.">
				</div>
				<button type="button" class="btn-action btn-add"
					id="add-waypoint-btn" title="경유지 추가">+</button>
			</div>
			</div>

			<input type="button" value="이 코스 등록하기" id="btnRegisterCourse">
		</div>

		<div id="map"></div>
	</div>
	</div>

	<%@include file="/WEB-INF/views/inc/courseasset.jsp"%>
	<script src="${pageContext.request.contextPath}/asset/js/courseregister.js"></script> 

</body>
</html>