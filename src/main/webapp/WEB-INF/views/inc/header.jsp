<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<header id="header">
	<!-- <h1>올데이런</h1> -->
	<!-- 상단 메뉴 -->
	<div class="header-content">
		<div class="logo-area">
			<c:if test="${sessionScope.role == '관리자'}">
			<a class="logo-link"
				href="${pageContext.request.contextPath}/admin/admin.do"> AllDayRun </a>
			</c:if>
			<c:if test="${sessionScope.role == '일반' || sessionScope.role == null}">
			<a class="logo-link"
				href="${pageContext.request.contextPath}/index.do"> AllDayRun </a>
			</c:if>
		</div>
		
		<c:choose>
			<%-- 로그인 했을시 --%>
			<%-- <c:when test="${not empty sessionScope.id}"> --%>
			<c:when test="${not empty sessionScope.accountId}">

				<div class="buttons">
					<a href="#"><span>마이페이지</span></a> <a
						href="${pageContext.request.contextPath}/user/logout.do"><span>로그아웃</span></a>
				</div>

			</c:when>
			<%-- 미로그인시 --%>
			<c:otherwise>

				<div class="buttons">
					<a href="${pageContext.request.contextPath}/user/login.do"><span>로그인</span></a>
					<a href="${pageContext.request.contextPath}/user/registerselect.do"><span>회원가입</span></a>
				</div>

			</c:otherwise>

		</c:choose>

	</div>
</header>