<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
	
		/* body {
		font-family: 'Noto Sans KR', sans-serif;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100vh;
		background-color: #fff;
		margin: 0;
	} */

	.container {
		display: flex;
		justify-content: center;
		align-items: center;
		gap: 120px;
		height: 70vh; /* 화면 높이의 80%만 차지해 중앙 배치 */
		text-align: center;
	}

	.option {
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.option h3 {
		font-size: 14px;
		color: #666;
		margin-bottom: 8px;
	}

	.option .title {
		font-size: 24px;
		font-weight: 600;
		color: #333;
		margin-top: 18px;
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
    		<!-- 여기 태그 내부에 본인 담당 페이지 html를 작성 -->
    			<div class="container">
					<div class="option" onclick="location.href='registerstep1.do'">
						<h3>일반 회원가입</h3>
						<div class="title">올데이런</div>
					</div>
			
					<div class="option">
						<h3>소셜 로그인</h3>
						
						<div id="g_id_onload"
						     data-client_id="757281038435-f89f408pe749euhcgbrct110vfjgp2au.apps.googleusercontent.com"
						     data-context="signin"
						     data-ux_mode="redirect"
						     data-login_uri="http://localhost:8080/user/googleoauthcallback.do"
						     data-auto_prompt="false">
						</div>
						
						<div class="g_id_signin"
						     data-type="icon"
						     data-shape="square"
						     data-theme="outline"
						     data-text="signup_with"
						     data-size="large"
						     data-locale="ko"
						     style="display: flex; justify-content: center;">
						</div>
			
					</div>
				</div>
    			
    		</div>
    		
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	<script src="https://accounts.google.com/gsi/client" async defer></script>
	<script>
		
	</script>
</body>
</html>