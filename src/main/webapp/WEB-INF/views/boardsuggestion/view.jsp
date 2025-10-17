<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
	<link rel="stylesheet" href="/alldayrun/asset/css/boardsuggestion.css">
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
				<div class="title">${dto.title}</div>
				<div class="info">
					작성자: ${dto.name} | 작성일: ${dto.regdate} | 조회수: ${dto.readCount}
				</div>
				<hr>
				<div class="content">${dto.content}</div>
				<br><br>
				<c:if test="${not empty dto.attach}">
					<div>첨부파일: 
						<a href="/asset/pic/${dto.attach}" download>${dto.attach}</a>
					</div>
				</c:if>
				
			
				<div class="btns">
				<c:if test="${not empty accountId}">
					<a href="/alldayrun/boardsuggestion/edit.do?boardContentSeq=${dto.boardContentSeq}">수정</a>
					<a href="/alldayrun/boardsuggestion/del.do?boardContentSeq=${dto.boardContentSeq}">삭제</a>
				</c:if>
					<a href="/alldayrun/boardsuggestion/list.do">목록</a>
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