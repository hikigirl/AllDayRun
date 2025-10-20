<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
	<title>크루 가입 요청 중...</title>

	<style>
		.join-loader {
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			height: 80vh;
			text-align: center;
			color: #444;
		}
		
		.spinner {
			width: 60px;
			height: 60px;
			border: 6px solid #ddd;
			border-top: 6px solid #4CAF50;
			border-radius: 50%;
			animation: spin 1s linear infinite;
			margin-bottom: 20px;
		}
		@keyframes spin {
			0% { transform: rotate(0deg); }
			100% { transform: rotate(360deg); }
		}
	</style>
</head>
<body>
	<div id="layout">
  		<%@include file="/WEB-INF/views/inc/header.jsp"%>
  		<div class="content-wrapper">
    		<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>

    		<div class="main-content">
				<div class="join-loader">
					<div class="spinner"></div>
					<h2>크루 가입 신청 중입니다...</h2>
					<p>잠시만 기다려주세요. 자동으로 메인 페이지로 이동합니다.</p>
				</div>

				<form id="autoForm" method="POST" action="/alldayrun/crewjoin.do">
					<input type="hidden" name="crewSeq" value="${param.crewSeq}">
				</form>
    		</div>
  		</div>
	</div>

	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>

	<script>
	window.onload = function() {
		setTimeout(function() {
			document.getElementById("autoForm").submit();
		}, 2000); // 2초(2000ms) 후 자동 전송
	};
</script>
</body>
</html>
