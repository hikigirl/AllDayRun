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
				    <h2>🗑️ 건의 게시글 삭제</h2>
				
				    <div class="info-box">
				        <p><strong>제목 :</strong> ${dto.title}</p>
				        <p><strong>작성자 :</strong> ${dto.name}</p>
				        <p><strong>작성일 :</strong> ${dto.regdate}</p>
				        <p><strong>내용</strong></p>
				        <div style="background:#fff; border:1px solid #eee; padding:10px; border-radius:6px;">
				            ${dto.content}
				        </div>
				    </div>
				
				    <p class="confirm-text">⚠️ 정말 이 게시글을 삭제하시겠습니까?</p>
				
				    <form id="deleteForm" method="POST" action="/alldayrun/boardsuggestion/del.do">
				        <input type="hidden" name="boardContentSeq" value="${dto.boardContentSeq}">
				        <div class="btns">
				            <a href="/alldayrun/boardsuggestion/view.do?boardContentSeq=${boardContentSeq}" class="btn-back">돌아가기</a>
				            <button type="submit" class="btn-delete" onclick="confirmDelete()">삭제하기</button>
				        </div>
				    </form>
				</div>
				
    		</div>
    		
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
		
	</script>
</body>
</html>