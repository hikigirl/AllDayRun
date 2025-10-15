<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<header id="header">
	<!-- <h1>올데이런</h1> -->
	<!-- 상단 메뉴 -->
	<div class="header-content">
		<div class="logo-area">
			<a class="logo-link"
				href="${pageContext.request.contextPath}/template.do"> AllDayRun
			</a>
		</div>

		<div class="buttons">
			<button>로그인</button>
			<button>회원가입</button>
		</div>
	</div>
</header>