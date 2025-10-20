<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/views/inc/asset.jsp"%>

<style>
/* 기본 스타일 유지 (이전 코드 그대로) */
body {
	background: linear-gradient(135deg, #e9f0ff, #ffffff);
	font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
	color: #222;
	margin: 0;
}

.main-content {
	flex: 1;
	padding: 50px;
	background: #fff;
	border-radius: 18px;
	box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
	margin: 50px;
}
.main-content h1 {
	font-size: 28px;
	font-weight: 700;
	color: #1f2a56;
	margin-bottom: 35px;
	border-left: 6px solid #4f6ef7;
	padding-left: 14px;
}
.summary-container {
	display: flex;
	gap: 20px;
	margin-bottom: 30px;
}
.summary-card {
	flex: 1;
	background: #f7f9ff;
	border-radius: 14px;
	padding: 20px 25px;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
	transition: all 0.3s ease;
	text-align: center;
}
.summary-card:hover {
	transform: translateY(-4px);
	box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}
.summary-card h3 {
	font-size: 15px;
	color: #4f6ef7;
	margin-bottom: 10px;
	font-weight: 600;
}
.summary-card p {
	font-size: 22px;
	font-weight: 700;
	color: #222;
}
table {
	width: 100%;
	border-collapse: collapse;
	overflow: hidden;
	border-radius: 12px;
	background: #fff;
}
thead {
	background: #4f6ef7;
	color: #fff;
}
th, td {
	padding: 16px 14px;
	text-align: center;
	border-bottom: 1px solid #e5e8f0;
}
tbody tr:hover {
	background-color: #f3f6ff;
	transition: background 0.2s ease;
}
th {
	font-weight: 600;
	letter-spacing: 0.3px;
}
td {
	font-size: 15px;
}
.btn {
	display: inline-block;
	padding: 8px 18px;
	border-radius: 8px;
	font-size: 14px;
	font-weight: 600;
	text-decoration: none;
	transition: all 0.25s ease;
}
.btn-success {
	background-color: #4f6ef7;
	color: #fff;
}
.btn-success:hover {
	background-color: #3554db;
	box-shadow: 0 3px 8px rgba(79, 110, 247, 0.35);
	transform: translateY(-2px);
}
.btn-danger {
	background-color: #ff5f5f;
	color: #fff;
}
.btn-danger:hover {
	background-color: #e04848;
	box-shadow: 0 3px 8px rgba(255, 95, 95, 0.35);
	transform: translateY(-2px);
}
.btn-secondary {
	background-color: #888;
	color: #fff;
	cursor: default;
}
td[colspan="5"] {
	color: #888;
	background-color: #fafafa;
	font-style: italic;
}
@media ( max-width : 900px) {
	.main-content {
		margin: 20px;
		padding: 25px;
	}
	table th, table td {
		font-size: 13px;
		padding: 10px 6px;
	}
	.summary-container {
		flex-direction: column;
	}
}
</style>
</head>
<body>

<div id="layout">
	<%@include file="/WEB-INF/views/inc/header.jsp"%>
	<div class="content-wrapper">
		<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
		<div class="main-content">
			<h1>크루 가입 요청 목록</h1>

			<!-- 요약 카드 -->
			<div class="summary-container">
				<div class="summary-card">
					<h3>총 요청 수</h3>
					<p>${fn:length(requestList)}</p>
				</div>
				<div class="summary-card">
					<h3>승인 대기</h3>
					<p>
						<c:set var="pendingCount" value="0"/>
						<c:forEach items="${requestScope.requestList}" var="dto">
							<c:if test="${dto.requestState == '대기'}">
								<c:set var="pendingCount" value="${pendingCount + 1}"/>
							</c:if>
						</c:forEach>
						${pendingCount}
					</p>
				</div>
				<div class="summary-card">
					<h3>처리 완료</h3>
					<p>
						<c:set var="doneCount" value="0"/>
						<c:forEach items="${requestScope.requestList}" var="dto">
							<c:if test="${dto.requestState == '승인' || dto.requestState == '거절'}">
								<c:set var="doneCount" value="${doneCount + 1}"/>
							</c:if>
						</c:forEach>
						${doneCount}
					</p>
				</div>
			</div>

			<table>
				<thead>
					<tr>
						<th>번호</th>
						<th>ID</th>
						<th>닉네임</th>
						<th>상태</th>
						<th>관리</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${requestScope.requestList}" var="dto">
						<tr>
							<td>${dto.crewJoinSeq}</td>
							<td>${dto.accountId}</td>
							<td>${dto.nickname}</td>
							<td>${dto.requestState}</td>
							<td>
								<c:choose>
									<c:when test="${dto.requestState == '대기'}">
										<form method="post" action="/alldayrun/crewjoinapprove.do" style="display:inline;">
											<input type="hidden" name="crewJoinSeq" value="${dto.crewJoinSeq}">
											<input type="hidden" name="crewSeq" value="${crewSeq}">
											<button type="submit" class="btn btn-success btn-sm">승인</button>
										</form>
										<form method="post" action="/alldayrun/crewjoinreject.do" style="display:inline;">
											<input type="hidden" name="crewJoinSeq" value="${dto.crewJoinSeq}">
											<input type="hidden" name="crewSeq" value="${crewSeq}">
											<button type="submit" class="btn btn-danger btn-sm">거절</button>
										</form>
									</c:when>
									<c:otherwise>
										<button class="btn btn-secondary btn-sm" disabled>처리 완료</button>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${empty requestScope.requestList}">
						<tr>
							<td colspan="5">가입 요청이 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
</body>
</html>
