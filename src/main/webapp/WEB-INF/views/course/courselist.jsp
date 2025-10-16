<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
	<link rel="stylesheet" href="/alldayrun/asset/css/coursemain.css" />
	<link rel="stylesheet" href="/alldayrun/asset/css/courselist.css" />
</head>
<body>
	
	<div id="layout">
		<!-- 헤더 -->
  		<%@include file="/WEB-INF/views/inc/header.jsp"%>

 		 <!-- 사이드바 + 메인컨텐츠 -->
  		<div class="content-wrapper">
    		<!-- 메인 컨텐츠 영역 -->
    		<div class="main-content" id="courseList-main">
    		<!-- 여기 태그 내부에 본인 담당 페이지 html를 작성 -->
    			<form id="searchBox" action="/alldayrun/course/coursemain.do" method="GET">
                    <input type="text" id="searchKeyword" name="keyword" placeholder="검색어 입력" value="${keyword}"/>
                    <button type="submit" id="searchButton">검색</button>
                </form>
                <div id="recommend-container">
                    <div id="message" class="message title">전체 코스 목록</div>
                    
                    <div class="course-grid">
                        <c:if test="${empty courseList}"><p>표시할 코스가 없습니다.</p></c:if>
                        <c:forEach var="course" items="${courseList}">
                        	<a href="/alldayrun/course/coursedetail.do?courseSeq=${course.courseSeq}" class="course-card-link">
					            <div class="course-card">
					                <div class="card-map">
					                    <img src="/alldayrun/asset/courseimg/card_map_sample.png" alt="코스 지도 예시">
					                </div>
					                <div class="card-details">
					                    <h3 class="course-title">${course.courseName}</h3>
					                    <div class="course-info">
					                        <span>거리</span> <span class="kilometers">${course.totalDistance}km</span>
					                    </div>
					                    <div class="course-info">
					                        <span>즐겨찾기</span> <span>${course.favoriteCount}</span>
					                    </div>
					                    <div class="course-curator">${course.curator}</div>
					                </div>
					            </div>
				        	</a>
                        </c:forEach>
                    </div>

                    <div class="pagination">
                        <c:if test="${pageDTO.startPage > 1}">
                            <a href="/alldayrun/course/courselist.do?page=${pageDTO.startPage - 1}">&laquo;</a>
                        </c:if>

                        <c:forEach var="p" begin="${pageDTO.startPage}" end="${pageDTO.endPage}">
                            <a href="/alldayrun/course/courselist.do?page=${p}" class="${pageDTO.currentPage == p ? 'active' : ''}">${p}</a>
                        </c:forEach>

                        <c:if test="${pageDTO.endPage < pageDTO.totalPage}">
                            <a href="/alldayrun/course/courselist.do?page=${pageDTO.endPage + 1}">&raquo;</a>
                        </c:if>
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