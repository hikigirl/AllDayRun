<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
.login-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 600px;
}

.login-container {
    background-color: #fff;
    padding: 30px 40px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0,0,0,0.1);
    text-align: center;
    width: 360px;
}
h2 {
	margin-bottom: 5px;
}
.subtitle {
	font-size: 14px;
	color: #777;
	margin-bottom: 20px;
}
input[type="text"], input[type="password"] {
            width: 100%;
            padding: 12px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .login-btn {
            background-color: #4285F4;
            color: white;
            border: none;
            width: 100%;
            padding: 12px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .login-btn:hover {
            background-color: #357ae8;
        }
        .social-login {
            margin-top: 20px;
        }
        .social-login img {
            width: 40px;
            margin: 0 10px;
            cursor: pointer;
        }
        .find-links {
            margin-top: 15px;
        }
        .find-links a {
            font-size: 13px;
            color: #555;
            text-decoration: none;
            margin: 0 5px;
        }
        .error-msg {
            color: red;
            margin-bottom: 10px;
            font-size: 13px;
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
	    		<div class="login-wrapper">
		    		<div class="login-container">
					    <h2>달리자 나답게! 올데이런에 오신 것을 환영해요👋</h2>
					    <div class="subtitle">즐거운 러닝생활을 시작하세요!</div>
					
					    <form action="${pageContext.request.contextPath}/user/login.do" method="post">
					        <div class="error-msg">
					            <div class="error-msg">
					    			${error}
								</div>
					
					        </div>
					        <input type="text" name="accountId" placeholder="이메일을 입력하세요." required />
					        <input type="password" name="password" placeholder="비밀번호를 입력하세요." required />
					        <button type="submit" class="login-btn">로그인</button>
					    </form>
					
					    <div class="find-links">
					        <a href="/user/findid.jsp">아이디 찾기</a> | 
					        <a href="/user/findpw.jsp">비밀번호 찾기</a>
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
	
	<script>
		
	</script>
</body>
</html>