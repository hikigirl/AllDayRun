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
    			
    			<form method="POST" action="/alldayrun/boardsuggestion/edit.do">
   				<table>
   					<tr>
						<th>제목</th>
						<td><input type="text" name="title" id="title" required class="full" value="${dto.title}"></td>
					</tr>
					<tr>
						<th>내용</th>
						<td><textarea name="content" id="content" required class="full">${dto.content}</textarea></td>
					</tr>
   				</table>
   				<div>
					<button type="button" onclick="location.href='/alldayrun/boardsuggestion/view.do?boardContentSeq=${dto.boardContentSeq}';">돌아가기</button>
					<button type="submit">수정하기</button>
				</div>
				<input type="hidden" name="boardContentSeq" value="${dto.boardContentSeq}">
    			</form>
    			
    		</div>
    		
  		</div>
	</div>
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
	</script>
</body>
</html>