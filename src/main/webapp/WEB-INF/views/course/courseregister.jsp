<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
	#map {
		width: 600px;
		height: 450px;
	}
</style>
</head>
<body>
	<%@include file="/WEB-INF/views/inc/header.jsp"%>
	<div>코스 등록하기</div>
	<div>클릭하면 마커가 생성됩니다..</div>
	<div id="map"></div>
	<!-- <div id="clickLatlng"></div> -->
	<div>출발지: <input type="text" name="placeStart" id="placeStart" placeholder="출발지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via1" id="via1" placeholder="경유지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via2" id="via2" placeholder="경유지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via3" id="via3" placeholder="경유지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via4" id="via4" placeholder="경유지를 입력하세요."/></div>
	<div>경유지: <input type="text" name="via5" id="via5" placeholder="경유지를 입력하세요."/></div>
	<div>도착지: <input type="text" name="placeEnd" id="placeEnd" placeholder="도착지를 입력하세요."/><span id="addVia">&nbsp;+&nbsp;</span></div>
	
	<div><input type="button" value="지점 추가하기" id="btnAdd" /></div>
	
	<input type="hidden" name="lat" id="lat">
	<input type="hidden" name="lng" id="lng">
	<input type="hidden" name="place" id="place"/>

	
	<%@include file="/WEB-INF/views/inc/courseasset.jsp"%>
	<script src="${pageContext.request.contextPath}/asset/js/courseregister.js"></script>

</body>
</html>