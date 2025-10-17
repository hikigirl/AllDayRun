<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
/* main-content 안에서만 적용 */
 .main-content {
	width: 1200px;
	margin: 40px auto;
	padding: 30px;
	background: #fff;
	border-radius: 18px;
	box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
	animation: fadeIn 0.5s ease-in-out;
	font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
	color: #333;
}

/* 테이블 스타일 */
.main-content .table {
	width: 100%;
	border-collapse: collapse;
	border-radius: 12px;
	overflow: hidden;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
	margin-bottom: 20px;
}

.main-content .table thead {
	background-color: #4f6ef7;
	color: #fff;
}

.main-content .table th, .main-content .table td {
	padding: 14px 12px;
	text-align: center;
	border-bottom: 1px solid #e2e8f0;
}

.main-content .table tbody tr:hover {
	background-color: #f3f6ff;
	transition: background 0.2s ease;
}

.main-content .table a {
	color: #4f6ef7;
	text-decoration: none;
	font-weight: 500;
}

.main-content .table a:hover {
	text-decoration: underline;
}

/* 글쓰기 버튼 */
.main-content .write-btn-container {
	text-align: right;
	margin-top: 20px;
}

.main-content .write-btn-container button {
	padding: 10px 22px;
	border-radius: 10px;
	background-color: #4f6ef7;
	color: #fff;
	border: none;
	font-weight: 600;
	cursor: pointer;
	transition: all 0.3s ease;
}

.main-content .write-btn-container button:hover {
	background-color: #3554db;
	box-shadow: 0 4px 12px rgba(79, 110, 247, 0.3);
	transform: translateY(-2px);
}

/* 페이지네이션 */
.main-content .pagination {
	display: inline-block;
	padding-left: 0;
	margin: 20px 0;
	border-radius: 4px;
}

.main-content .pagination>li {
	display: inline;
}

.main-content .pagination>li>a, .main-content .pagination>li>span {
	position: relative;
	float: left;
	padding: 8px 14px;
	margin-left: -1px;
	line-height: 1.4;
	color: #4f6ef7;
	text-decoration: none;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 6px;
	transition: all 0.2s ease;
}

.main-content .pagination>li>a:hover, .main-content .pagination>li>span:hover
	{
	background-color: #f0f4ff;
}

.main-content .pagination>.active>a, .main-content .pagination>.active>span
	{
	z-index: 3;
	color: #fff;
	cursor: default;
	background-color: #4f6ef7;
	border-color: #4f6ef7;
}

.main-content .pagination>.disabled>a, .main-content .pagination>.disabled>span
	{
	color: #aaa;
	cursor: not-allowed;
	background-color: #fff;
	border-color: #ddd;
}

/* 반응형 */
@media ( max-width : 1300px) {
	.main-content {
		width: 95%;
	}
	.main-content .table th, .main-content .table td {
		font-size: 14px;
		padding: 10px 6px;
	}
	.main-content .write-btn-container {
		text-align: center;
	}
}

/* 애니메이션 */
@
keyframes fadeIn {from { opacity:0;
	transform: translateY(20px);
}

to {
	opacity: 1;
	transform: translateY(0);
}

}
.main-content .board-title {
	font-size: 26px;
	font-weight: 700;
	color: #1f2a56;
	margin-bottom: 20px;
	border-left: 6px solid #4f6ef7;
	padding-left: 12px;
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


			<div class="main-content crewmain" id="">
				<h2 class="board-title">크루 자유게시판</h2>
				<table class="table">
					<thead>
						<tr>
							<th>제목</th>
							<th>작성자</th>
							<th>조회수</th>
							<th>좋아요</th>
							<th>작성일</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="dto">
							<tr>
								<td><a
									href="/alldayrun/crewboardview.do?boardContentSeq=${dto.boardContentSeq}">${dto.title}</a></td>
								<td>${dto.nickname}</td>
								<td>${dto.readCount}</td>
								<td>${dto.favoriteCount}</td>
								<td>${dto.regdate}</td>
							</tr>
						</c:forEach>
						<c:if test="${empty list}">
							<tr>
								<td colspan="7">게시글이 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
				<div style="text-align: center;">${pagebar}</div>
				<div class="write-btn-container">
					<button onclick="location.href='/alldayrun/crewboardadd.do'">글쓰기</button>
				</div>
			</div>

		</div>
	</div>


	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>

	<script>
		
	</script>
</body>
</html>