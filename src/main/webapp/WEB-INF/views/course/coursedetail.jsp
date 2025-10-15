<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>

</head>
<body>
	
	<div id="layout">
		<!-- 헤더 -->
  		<%@include file="/WEB-INF/views/inc/header.jsp"%>

 		 <!-- 사이드바 + 메인컨텐츠 -->
  		<div class="content-wrapper">
    		<!-- 메인 컨텐츠 영역 -->
    		<div class="main-content" id="courseDetail-main">
    		<!-- 여기 태그 내부에 본인 담당 페이지 html를 작성 -->
    			<h2>코스 상세 정보</h2>
    			<div>지도 컨테이너(이미지 지도 출력)</div>
    			<hr>
    			<div>
    				코스 정보 컨테이너
    				<div>코스명</div>
    				<div>거리</div>
    				<div>등록자</div>
    			</div>
    			<hr>
    			<div>즐겨찾기 버튼</div>
    			<div>본인이 등록한 코스인 경우 삭제 요청 버튼</div>
    			<hr>
    			<div>
    				코스 후기(댓글) 컨테이너
    				<div>
    					후기1
    					<div>작성자</div>
    					<div>작성일</div>
    					<div>댓글내용</div>
    				</div>
    			
    			</div>
    			
    		</div>
    		
    		<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
		
	</script>
</body>
</html>