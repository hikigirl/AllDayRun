/**
 * 코스 상세 페이지의 지도를 생성하고 경로를 표시하는 스크립트
 */
$(document).ready(function() {
    
    // 1. JSP가 HTML에 숨겨둔 데이터 컨테이너를 찾습니다.
    const $dataContainer = $('#spot-data-container');
    
    // 2. data-spots 속성에서 JSON 데이터를 읽어옵니다.
    // jQuery의 .data() 메서드는 JSON 문자열을 자동으로 JavaScript 객체로 변환해 줍니다.
    const spotList = $dataContainer.data('spots');
    
    // 3. 지점 데이터가 존재하고, 1개 이상일 때만 지도 생성 함수를 호출합니다.
    if (spotList && spotList.length > 0) {
        drawCourseMap(spotList);
		updateAddresses(spotList);
    } else {
        // 데이터가 없을 경우 콘솔에 로그를 남겨 디버깅을 돕습니다.
        console.error("지도에 표시할 지점 데이터(spotList)가 없습니다.");
    }
});

/**
 * ✅ [추가] 각 지점의 좌표를 주소로 변환하고 UI를 업데이트하는 함수
 * @param {Array} spots - 지점 정보 배열
 */
function updateAddresses(spots) {
    // 1. 카카오맵의 Geocoder 객체를 생성합니다.
    const geocoder = new kakao.maps.services.Geocoder();

    // 2. 모든 지점을 순회하며 주소 변환을 요청합니다.
    spots.forEach((spot, index) => {
        
        // 3. coord2Address 함수로 좌표를 주소로 변환합니다. (비동기 처리)
        geocoder.coord2Address(spot.lng, spot.lat, function(result, status) {
            
            // 4. 변환이 성공하면, 해당 주소 영역을 찾아 텍스트를 업데이트합니다.
            if (status === kakao.maps.services.Status.OK) {
                const roadAddress = result[0].road_address ? result[0].road_address.address_name : result[0].address.address_name;
                
                // JSP에서 만든 고유 ID를 사용해 정확한 위치를 찾아갑니다.
                $(`#address-${index}`).text(roadAddress);
            } else {
                // 변환 실패 시
                $(`#address-${index}`).text("주소를 불러올 수 없습니다.");
            }
        });
    });
}


/**
 * 전달받은 지점 목록(spots)을 사용해 지도에 마커와 경로를 표시하는 함수
 * @param {Array} spots - 지점 정보({place, lat, lng})가 담긴 배열
 */
function drawCourseMap(spots) {
    // 1. 지도를 표시할 div와 지도 옵션을 설정합니다.
    const mapContainer = document.getElementById('map-container'); 
    const mapOption = {
        center: new kakao.maps.LatLng(spots[0].lat, spots[0].lng), // 첫 번째 지점을 중심으로 설정
        level: 8, // 초기 확대 레벨
        // 상세 페이지에서는 사용자가 지도를 조작할 필요가 없으므로 비활성화합니다.
        draggable: false,
        scrollwheel: false,
        disableDoubleClickZoom: true
    };

    // 2. 지도를 생성합니다.
    const map = new kakao.maps.Map(mapContainer, mapOption);

    // 3. 지점들을 순회하며 마커를 생성하고 경로/범위를 계산합니다.
    const path = []; // 경로(Polyline)를 그릴 좌표 배열
    const bounds = new kakao.maps.LatLngBounds(); // 모든 마커가 보이도록 지도의 범위를 조절할 객체

    spots.forEach(spot => {
        const position = new kakao.maps.LatLng(spot.lat, spot.lng);
        
        // 마커 생성
        const marker = new kakao.maps.Marker({
            position: position
        });
        marker.setMap(map);
        
        // 경로 배열과 범위 객체에 현재 좌표 추가
        path.push(position);
        bounds.extend(position);
    });

    // 4. 지점이 2개 이상일 경우 경로(Polyline)를 그립니다.
    if (path.length > 1) {
        const polyline = new kakao.maps.Polyline({
            path: path,
            strokeWeight: 5,
            strokeColor: '#FF0000', // 선 색깔
            strokeOpacity: 0.7,
            strokeStyle: 'solid'
        });
        polyline.setMap(map);
    }
    
    // 5. [핵심] 모든 마커와 경로가 한눈에 보이도록 지도의 범위(Bounds)를 재설정합니다.
    map.setBounds(bounds);
}