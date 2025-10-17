<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
	<link rel="stylesheet" href="/alldayrun/asset/css/admin.css">
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
				
			<!-- 코스 신청 관리 -->
			<h2 class="section-title">코스 신청 관리</h2>
			<table class="course-list">
				<thead>
					<tr>
						<th>코스명</th>
						<th>아이디</th>
						<th>상태</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="dto">
						<c:if test="${dto.courseApproval == '대기'}">
							<tr>
								<td><a href="/alldayrun/admin/admincoursedetail.do?courseSeq=${dto.courseSeq}" >${dto.courseName}</a></td>
								<td>${dto.accountId}</td>
								<td>${dto.courseApproval}</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			
			<!-- 코스 승인 현황 -->
			<h2 class="section-title">코스 승인 현황</h2>
			<table class="course-list">
				<thead>
					<tr>
						<th>코스명</th>
						<th>아이디</th>
						<th>상태</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="dto">
						<c:if test="${dto.courseApproval == '승인'}">
							<tr>
								<td><a href="/alldayrun/course/coursedetail.do?courseSeq=${dto.courseSeq}" >${dto.courseName}</a></td>
								<td>${dto.accountId}</td>
								<td>${dto.courseApproval}</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
				
    		</div>
    		
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
		
	</script>
</body>
</html>