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
		    <h2>📋 건의 게시판</h2>
		
		    <table>
		        <thead>
		            <tr>
		                <th style="width: 50px;">번호</th>
		                <th>제목</th>
		                <th style="width: 120px;">이름</th>
		                <th style="width: 180px;">날짜</th>
		                <th style="width: 60px;">읽음</th>
		            </tr>
		        </thead>
		        <tbody>
		            <c:forEach var="dto" items="${list}">
		                <tr>
		                    <td>${dto.boardContentSeq}</td>
		                    <td class="title">
		                        <a href="/alldayrun/boardsuggestion/view.do?boardContentSeq=${dto.boardContentSeq}">
		                            ${dto.title}
		                        </a>
		                    </td>
		                    <td>${dto.name}</td>
		                    <td>${dto.regdate}</td>
		                    <td>${dto.readCount}</td>
		                </tr>
		            </c:forEach>
		            <c:if test="${empty list}">
		                <tr>
		                    <td colspan="5" style="text-align:center; color:#999;">등록된 게시글이 없습니다.</td>
		                </tr>
		            </c:if>
		        </tbody>
		    </table>

			    <div class="btns">
			    <c:if test="${not empty accountId}">
			        <a href="/alldayrun/boardsuggestion/add.do">✏️ 쓰기</a>
			    </c:if>
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




