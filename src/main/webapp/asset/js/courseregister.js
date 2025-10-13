//지도를 담을 영역의 DOM 레퍼런스
var container = document.getElementById('map');

//지도를 생성할 때 필요한 기본 옵션
var options = {

	//지도의 중심좌표.
	center : new kakao.maps.LatLng(37.51080190682596, 126.99716104149441),

	//지도의 레벨(확대, 축소 정도)
	level : 7
};

var map = new kakao.maps.Map(container, options);
//-----------------------------------------------------------

//let m;
const mlist = []; 			//마커 객체들을 저장할 배열
const waypoints = []; 		//서버로 보낼 지점 정보(좌표와 사용자 입력 장소명)
let currentClickedCoords;   // 사용자가 마지막으로 클릭한 좌표를 임시 저장할 변수
// const geocoder = new kakao.maps.services.Geocoder(); // 주소-좌표 변환 객체


//지도 클릭 이벤트 - 좌표를 임시 변수에 저장 및 마커 표시
kakao.maps.event.addListener(map, 'click', event => {
	// if (m != null) {
	// 	m.setMap(null);
	// }
	
	if (waypoints.length >= 7) {
        alert('지점은 최대 7개까지 추가할 수 있습니다.');
        return;
    }

	//클릭한 위치에 마커 생성
	const marker = new kakao.maps.Marker({
		position: event.latLng
	});
	
	marker.setMap(map);
	mlist.push(marker); //생성된 마커를 배열에 추가

	//클릭한 좌표를 임시 변수에 저장
	currentClickedCoords = event.latLng;
	$('#spotName').focus();
	
	//클릭한 좌표를 hidden태그에 넣기 -> 배열로 인해 불필요해짐
	// $('input[name=lat]').val(event.latLng.getLat());
	// $('input[name=lng]').val(event.latLng.getLng());

	
});
//지점 추가 버튼 클릭 이벤트 - waypoints 배열에 데이터 추가
$('#btnAddSpot').on('click', () => {
	const spotName = $('#spotName').val();
	if (!currentClickedCoords) {
        alert('먼저 지도에서 위치를 클릭해주세요.');
        return;
    }
    if (!spotName) {
        alert('장소명을 입력해주세요.');
        $('#spotName').focus();
        return;
    }
    
    // waypoints 배열에 사용자 입력값과 좌표를 객체로 저장
    waypoints.push({
        place: spotName, // 사용자가 직접 입력한 장소명
        lat: currentClickedCoords.getLat(),
        lng: currentClickedCoords.getLng()
    });

    // 사용자에게 피드백을 주기 위해 목록에 추가
    $('#spotList ul').append(`<li>${spotName}</li>`);
    console.log('현재 지점 목록:', waypoints);

    // 다음 입력을 위해 입력 필드와 임시 좌표 변수 초기화
    $('#spotName').val('');
    currentClickedCoords = null;
});


//코스 등록 버튼 클릭 이벤트 -> 서버에 배열 데이터 전송
$('#btnRegister').on('click', () => {
	//ajax 처리, courseregister.do에서...
	//alert();
	
	// 출발, 도착지는 필수이므로 최소 2개 이상의 지점이 있어야 함
    if (waypoints.length < 2) {
        alert('출발지와 도착지는 필수 입력사항입니다.');
        return;
    }

	// const lat = $('#lat').val();
	// const lng = $('#lng').val();
	// const place = $('#place').val();
	
	// if((lat=='')||(lng=='')) {
	// 	alert('지도에 마커를 먼저 추가해주세요.');
	// 	return;
	// }
	
	$.ajax({
		type: 'post',
		url: '/alldayrun/course/courseregister.do',
		data: JSON.stringify(waypoints),
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(result) {
			if (result.result === 'success') {
                alert('코스 등록이 완료되었습니다.');
                location.href = '/course/coursesearch.do'; // 성공 시 메인 페이지 등으로 이동
            } else {
                alert('코스 등록에 실패했습니다. 다시 시도해주세요.');
            }
			
		},
		error: function(a,b,c){
			console.log(a,b,c);
		}
	});
	
});