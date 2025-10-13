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

let m;
const mlist = [];



kakao.maps.event.addListener(map, 'click', event => {
	if (m != null) {
		m.setMap(null);
	}
	
	m = new kakao.maps.Marker({
		position: event.latLng
	});
	
	m.setMap(map);
	
	//클릭한 좌표를 hidden태그에 넣기
	$('input[name=lat]').val(event.latLng.getLat());
	$('input[name=lng]').val(event.latLng.getLng());
});

$('#btnAdd').on('click', () => {
	//ajax 처리, courseregister.do에서...
	//alert();
	
	const lat = $('#lat').val();
	const lng = $('#lng').val();
	const place = $('#place').val();
	
	if((lat=='')||(lng=='')) {
		alert('지도에 마커를 먼저 추가해주세요.');
		return;
	}
	
	$.ajax({
		type: 'post',
		url: '/alldayrun/course/courseregister.do',
		data: {
			lat: lat,
			lng: lng,
			place: place
		},
		dataType: 'json',
		success: function(result) {
			console.log(result);
			alert('마커 정보가 성공적으로 전송되었습니다.');
			
			// 성공 후 입력값 초기화
            $('input[name=lat]').val('');
            $('input[name=lng]').val('');
			$('#place').val('');
            if (m != null) {
				m.setMap(null);
			}
			
		},
		error: function(a,b,c){
			console.log(a,b,c);
		}
	});
	
});