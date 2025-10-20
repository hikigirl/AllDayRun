<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>All Day Run - 크루 생성</title>
<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
.form-container {
	width: 100%;
	max-width: 800px;
	margin: 0 auto;
	padding: 20px;
	border: 1px solid #ddd;
	border-radius: 8px;
	background-color: #fff;
}

.form-group {
	margin-bottom: 20px;
}


.form-group label {
	display: block;
	font-weight: bold;
	margin-bottom: 8px;
}

.form-group input[type="text"], .form-group input[type="file"],
	.form-group textarea {
	width: 100%;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

.form-group textarea {
	min-height: 120px;
	resize: vertical;
}

.form-actions {
	text-align: right;
}

.form-actions button {
	padding: 10px 20px;
	background-color: #337ab7;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 16px;
}

#map {
	width: 100%;
	height: 400px;
	margin-top: 10px;
}

.map-guide {
	font-size: 0.9em;
	color: #666;
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
	
			<div  class="main-content">
				<div class="form-container">
					<form method="POST" action="/alldayrun/crewregisterok.do"
						enctype="multipart/form-data" onsubmit="return validateForm()">

						<div class="form-group">
							<label for="crewName">크루 이름</label> <input type="text"
								id="crewName" name="crewName" required>
						</div>

						<div class="form-group">
							<label for="crewAttach">크루 대표 사진</label> <input type="file"
								id="crewAttach" name="crewAttach">
						</div>

						<div class="form-group">
							<label for="description">크루 소개</label>
							<textarea id="description" name="description" required></textarea>
						</div>

						<div class="form-group">
							<label>주요 활동 지역</label>
							<p class="map-guide">지도를 클릭하여 크루의 주요 활동 지역을 설정하세요.</p>
							<p id="selectedAddress" class="map-guide"></p>
							<div id="map"></div>
						</div>

						<!-- 숨겨진 필드: 위도, 경도 및 주소 정보를 서버로 전송 -->
						<input type="hidden" id="latitude" name="latitude"> 
						<input type="hidden" id="longitude" name="longitude">
						<input type="hidden" id="regionCity" name="regionCity"> 
						<input type="hidden" id="regionCounty" name="regionCounty"> 
						<input type="hidden" id="regionDistrict" name="regionDistrict">

						<div class="form-actions">
							<button type="submit">크루 생성하기</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script type="text/javascript"
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey=c91c3d26b1fb2e839abc90879d2ddd84&libraries=services"></script>
	<script>
        document.addEventListener("DOMContentLoaded", function() {
            var mapContainer = document.getElementById('map');
            var latInput = document.getElementById('latitude');
            var lonInput = document.getElementById('longitude');

            var mapOption = {
                center: new kakao.maps.LatLng(37.566826, 126.9786567), // 기본 중심: 서울시청
                level: 5
            };

            var map = new kakao.maps.Map(mapContainer, mapOption);
            var geocoder = new kakao.maps.services.Geocoder();

            var marker = new kakao.maps.Marker({
                position: map.getCenter(),
                map : map
                
            });
            marker.setMap(map);

            // 지도 클릭 이벤트
            kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
                var latlng = mouseEvent.latLng;
                marker.setPosition(latlng);

                // 위도, 경도 값을 hidden input에 저장
                latInput.value = latlng.getLat();
                lonInput.value = latlng.getLng();
                
                // 좌표로 주소 정보 요청 (역지오코딩)
                searchDetailAddrFromCoords(latlng, function(result, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        var detailAddr = !!result[0].road_address ? result[0].road_address.address_name : result[0].address.address_name;
                        console.log('클릭한 위치의 주소: ' + detailAddr);

                        // 주소 정보를 hidden input에 저장
                        document.getElementById('regionCity').value = result[0].address.region_1depth_name;
                        document.getElementById('regionCounty').value = result[0].address.region_2depth_name;
                        document.getElementById('regionDistrict').value = result[0].address.region_3depth_name;
                    }
                    
                    if (status === kakao.maps.services.Status.OK) {
                        var detailAddr = !!result[0].road_address ? result[0].road_address.address_name : result[0].address.address_name;
                        document.getElementById('selectedAddress').textContent = '우리 크루는 여기서  뛰어요🏃: ' + detailAddr;
                    }
                });
            });

            function searchDetailAddrFromCoords(coords, callback) {
                geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
            }
        });
        
        function validateForm() {
        	let lat = document.getElementById('latitude').value;
        	if (!lat) {
        		alert('지도에 활동 지역을 선택해주세요.');
        		return false;
        	}
        	return true;
        }
	</script>
</body>
</html>