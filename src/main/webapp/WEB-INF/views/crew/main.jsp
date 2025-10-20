<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
#main-content {
	padding: 20px;
}

.crew-sections-container {
	display: flex;
	gap: 30px;
}


.crew-section {
	padding: 20px;
	border-radius: 8px;
	background-color: #f9f9f9;
}

.crew-section.nearby {
	flex: 2;
}

.crew-section.popular {
	flex: 1;
}

.crew-section h2 {
	margin-top: 0;
	padding-bottom: 10px;
	border-bottom: 2px solid #337ab7;
}

.crew-card {
	border: 1px solid #ddd;
	border-radius: 8px;
	overflow: hidden;
	text-decoration: none;
	color: inherit;
	display: block;
	background-color: #fff;
	transition: transform 0.2s, box-shadow 0.2s;
}

.crew-card:hover {
	transform: translateY(-5px);
	box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.crew-card-image {
	width: 100%;
	background-color: #eee;
}

.crew-card-image img {
	width: 100%;
	height: 100%;
	object-fit: cover;
}

	.crew-card-content {
		padding: 15px;
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
	}
.crew-card-content h3 {
	font-size: 1.3em;
	margin: 0 0 5px;
}

	.crew-card-info p {
		margin: 5px 0;
		font-size: 0.8em;
		color: #666;
		text-align: right;
	}
.nearby-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	grid-template-rows: repeat(2, 1fr);
	gap: 20px;
}

.nearby-grid .crew-card-image {
	height: 150px;
}

.popular-list {
	display: flex;
	flex-direction: column;
	gap: 15px;
}

	.popular-list .crew-card {
		display: flex;
		gap: 10px;
	}
.popular-list .crew-card-image {
	width: 150px;
	height: 120px;
}

.popular-list .crew-card-content {
	flex: 1;
}

.create-crew-banner {
	margin-top: 40px;
	padding: 30px;
	text-align: center;
	background-color: #e9ecef;
	border-radius: 8px;
}

.create-crew-banner p {
	margin: 0 0 15px;
	font-size: 1.1em;
}

.btn-create {
	padding: 12px 25px;
	background-color: #337ab7;
	color: white;
	border: none;
	border-radius: 15px;
	cursor: pointer;
	font-size: 1.1em;
	text-decoration: none;
}

.location-check-btn {
	
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
			<div class="main-content" id="">
				<div class="crew-sections-container">

					<!-- 좌측: 내 주변 크루 -->
					<div class="crew-section nearby">
						<h2>
							내 주변 크루
							<button class="location-check-btn" name="location-check-btn">📍</button>
						</h2>

						<div class="nearby-grid">
							<c:forEach items="${nearbyCrewList}" var="crew">
								<a href="/alldayrun/crewview.do?crewSeq=${crew.crewSeq}"
									class="crew-card">
									<div class="crew-card-image">
										<img src="/alldayrun/crewmainFile/${crew.crewAttach}"
											alt="${crew.crewName}">
									</div>
									<div class="crew-card-content">
										<h3>${crew.crewName}</h3>
										<div class="crew-card-info">
																		<p>
																			<strong>지역:</strong> ${crew.regionCity} ${crew.regionCounty}
																		</p>
																		<p>
																			<strong>인원:</strong> ${crew.memberCount}명
																		</p>
																		<p>
																			<strong>크루장:</strong> ${crew.nickname}
																		</p>											<p>
												<strong>거리:</strong>${crew.distance } 
											</p>
										</div>
									</div>
								</a>
							</c:forEach>
						</div>
					</div>

					<!-- 우측: 인기 있는 크루 -->
					<div class="crew-section popular">
						<h2>인기 있는 크루</h2>
						<div class="popular-list">
							<c:forEach items="${popularCrewList}" var="crew">
								<a href="/alldayrun/crewview.do?crewSeq=${crew.crewSeq}"
									class="crew-card">
									<div class="crew-card-image">
										<img src="/alldayrun/crewmainFile/${crew.crewAttach}"
											alt="${crew.crewName}">
									</div>
									<div class="crew-card-content">
										<h3>${crew.crewName}</h3>
										<div class="crew-card-info">
											<p>
												<strong>인원:</strong> ${crew.memberCount}명
											</p>
											<p>
												<strong>크루장:</strong> ${crew.nickname}
											</p>
										</div>
									</div>
								</a>
							</c:forEach>
						</div>
					</div>
				</div>

				<!-- 하단: 크루 생성 배너 -->
				<div class="create-crew-banner">
					<p>가입하고 싶은 크루가 없으세요? 나만의 크루를 만들어보세요!</p>
					<a href="/alldayrun/crewregister.do" class="btn-create">크루 만들기</a>
				</div>
			</div>

		</div>
	</div>


	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>

<script>
// 'location-check-btn' 버튼 클릭 이벤트 처리
document.querySelector('.location-check-btn').addEventListener('click', function() {
    // Geolocation API를 지원하는지 확인
    if (navigator.geolocation) {
        // 현재 위치를 비동기적으로 가져옴
        navigator.geolocation.getCurrentPosition(function(position) {
            // 성공적으로 위치를 가져왔을 때 실행될 콜백 함수
            const lat = position.coords.latitude; // 위도
            const lng = position.coords.longitude; // 경도

            console.log('새로운 위치를 가져왔습니다. 위도: ' + lat + ', 경도: ' + lng);
            alert('주변 크루 목록을 갱신합니다.');

            const requestUrl = '/alldayrun/crewmain.do?lat=' + lat + '&lng=' + lng;
            fetch(requestUrl)
                .then(response => response.text()) // 응답을 텍스트(HTML)로 변환
                .then(html => {
                    // 문자열로 받은 HTML을 파싱하여 DOM 객체로 만듭니다.
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, "text/html");

                    // 새로 받은 HTML에서 '.nearby-grid' 부분을 찾습니다.
                    const newNearbyGrid = doc.querySelector('.nearby-grid');

                    // 현재 페이지의 '.nearby-grid' 부분을 새로 받은 내용으로 교체합니다.
                    document.querySelector('.nearby-grid').innerHTML = newNearbyGrid.innerHTML;
                    
                    console.log('내 주변 크루 목록을 성공적으로 갱신했습니다.');
                })
                .catch(error => {
                    console.error('크루 목록 갱신 중 오류 발생:', error);
                    alert('목록을 갱신하는 데 실패했습니다.');
                });

        }, function(error) {
            // 위치 정보를 가져오는 데 실패했을 때 실행될 콜백 함수
            console.error('Geolocation 오류: ' + error.message);
            alert('위치 정보를 가져오는 데 실패했습니다.');
        });
    } else {
        // Geolocation API를 지원하지 않는 브라우저일 경우
        alert('이 브라우저는 위치 정보 기능을 지원하지 않습니다.');
    }
});
</script>
</body>
</html>
