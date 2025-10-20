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
				    <h2>✏️ 건의 게시글 작성</h2>
				
					<form method="POST" action="/alldayrun/boardsuggestion/add.do" enctype="multipart/form-data">
				        <div>
				            <label for="title">제목</label>
				            <input type="text" name="title" id="title" required>
				        </div>
				
				        <div>
				            <label for="content">내용</label>
				            <textarea name="content" id="content" required></textarea>
				        </div>
				
				        <div class="btns">
				            <a href="/alldayrun/boardsuggestion/list.do" class="btn-back">돌아가기</a>
				            <button type="submit" class="btn-write">쓰기</button>
				        </div>
				    </form>
    			
    			</div>
    		
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
		
	</script>
</body>
</html>