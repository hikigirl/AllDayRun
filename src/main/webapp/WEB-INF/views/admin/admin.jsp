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
 	</div>

	<!-- 사이드바 + 메인 -->
	<div class="content-wrapper">
   		<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
   		<!-- 메인 컨텐츠 영역 -->
   		<div class="main-content" id="">
   		<!-- 여기 태그 내부에 본인 담당 페이지 html를 작성 -->

		<div class="dashboard">
		
		    <!-- 🔥 러닝 활동 -->
		    <div class="card">
		        <h3>🔥 러닝 활동</h3>
		        <h4>오늘의 인기 코스 TOP3</h4>
		        <ul class="list top3">
		        	<%-- <li><a href="/alldayrun/course/courseMain.do?courseName=${dto.courseName}">한강공원코스</a></li>
		        	<li><a href="/alldayrun/course/courseMain.do?courseName=${dto.courseName}">역삼역 주변코스</a></li>
		            <li><a href="/alldayrun/course/courseMain.do?courseName=${dto.courseName}">선릉역 주변코스</a></li> --%>
		            <c:forEach var="course" items="${popularList}">
		            <li><a href="/alldayrun/course/coursedetail.do?courseSeq=${course.courseSeq}">${course.courseName}</a></li>
		            </c:forEach>
		        </ul>
		
		        <h4>챌린지별 참여율</h4>
		        <div class="chart-container">
		            <canvas id="challengeChart"></canvas>
		        </div>
		    </div>
		
		    <!-- 💬 커뮤니티 관리 -->
		    <div class="card">
		        <h3>💬 커뮤니티 관리</h3>
		        <h4>미처리 리스트</h4>
		        <c:set var="suggestionCount" value="0" />
		        
		        <ul class="list">
		            <li>🎯 <a href="">신고</a> : 1</li>
		            <li>❓ <a href="">문의</a> : 13</li>
		            <c:forEach items="${list}" var="dto">
					    <c:if test="${dto.boardContentSeq != null}">
					        <c:set var="suggestionCount" value="${suggestionCount + 1}" />
					    </c:if>
					</c:forEach>
		            <li>📜 <a href="/alldayrun/boardsuggestion/list.do">건의</a> : ${suggestionCount}</li>
		        </ul>
		        <h4>승인 대기 현황</h4>
		        <ul class="list">
		            <li>👥 <a href="">크루</a> : 3</li>
		            
		            <c:set var="pendingCount" value="0" />
					<c:forEach items="${list}" var="dto">
					    <c:if test="${dto.courseApproval == '대기'}">
					        <c:set var="pendingCount" value="${pendingCount + 1}" />
					    </c:if>
					</c:forEach>
		            <li>📍 <a href="/alldayrun/admin/admincourse.do">코스</a> : ${pendingCount}</li>
		        </ul>
		    </div>
		
		    <!-- 🌏 회원 통계 -->
		    <div class="card" style="grid-column: span 2;">
		        <h3>🌏 회원 통계</h3>
		        <h4>등급별 회원 평균 거리</h4>
		        <div class="chart-container">
		            <canvas id="memberChart"></canvas>
		        </div>
		    </div>
		
		</div>
			
		<!-- Chart.js -->
		<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
		<script>
		// 🟡 챌린지별 참여율 (도넛 차트)
		new Chart(document.getElementById('challengeChart'), {
		    type: 'doughnut',
		    data: {
		        labels: ['나는 오늘도 달린다', '나는 걷기를 사랑해', '나만의 운동 루틴'],
		        datasets: [{
		            data: [32.7, 10.9, 21.6],
		            backgroundColor: ['#ffcc00', '#ff66cc', '#9999ff'],
		        }]
		    },
		    options: {
		        plugins: {
		            legend: {
		                position: 'bottom'
		            }
		        }
		    }
		});
		
		// 🟠 등급별 회원 평균 거리 (가로 바 차트)
		new Chart(document.getElementById('memberChart'), {
		    type: 'bar',
		    data: {
		        labels: ['Lv-1', 'Lv-2', 'Lv-3'],
		        datasets: [{
		            label: '평균 거리 (km)',
		            data: [40, 80, 120],
		            backgroundColor: ['#9999ff', '#ff9966', '#66cc66']
		        }]
		    },
		    options: {
		        indexAxis: 'y',
		        scales: {
		            x: {
		                beginAtZero: true
		            }
		        }
		    }
		});
		</script>
    		
  		</div>
	</div>
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>

</body>
</html>


