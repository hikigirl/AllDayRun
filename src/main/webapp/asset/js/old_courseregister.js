$(document).ready(function() {

	// ===================================
	// 1. 지도 생성 및 전역 변수 초기화
	// ===================================
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
	const geocoder = new kakao.maps.services.Geocoder(); // 주소-좌표 변환 객체
	//-----------------------------------------------------------

	let waypoints = [null, null]; //서버로 보낼 지점 정보(좌표와 사용자 입력 장소명)	
	const markers = [null, null]; 		//마커 객체들을 저장할 배열(출발, 도착지 공간을 미리 확보)

	//let currentClickedCoords;   // 사용자가 마지막으로 클릭한 좌표를 임시 저장할 변수
	let activeInputIndex = null;  // 현재 활성화된 입력 상자의 인덱스

	// ===================================
	// 2. 이벤트 핸들러
	// ===================================
	// [1] 입력 상자 클릭(focus) 시, 해당 상자를 '활성화'
	$(document).on('focus', '.custom-name-input', function() { //화살표함수 사용하면 에러남(this 관련..)
		activeInputIndex = $(this).closest('.input-group').data('index');
		console.log('활성화된 입력상자 index: ' + activeInputIndex);
		$('#guide-message').text('지도에서 원하는 위치를 클릭하세요.').css('color', 'blue');

	});
	// 포커스 빠져나갔을 때 메세지 내용을 초기화
	$(document).on('blur', '.custom-name-input', function() {
		// activeInputIndex = null;
		//console.log('Blur! activeInputIndex = null');
		$('#guide-message').text('별명 입력란을 클릭하여 활성화한 후, 지도에서 원하는 위치를 클릭하세요.').css('color', 'black');
	});

	// [2] 지도 클릭 이벤트 - 좌표를 임시 변수에 저장 및 마커 표시
	kakao.maps.event.addListener(map, 'mousedown', event => {
		if (activeInputIndex === null) {
			//alert('먼저 주소를 입력할 지점(출발지, 경유지, 도착지)의 입력란을 클릭해주세요.');
			console.log('Map Click Ignored: activeInputIndex is null');
			return;
		}
		console.log('Map Click Start: activeInputIndex = ' + activeInputIndex);

		const clickedPosition = event.latLng;
		// 좌표 -> 주소 변환
		// geocoder는 비동기이므로, activeInputIndex를 콜백 함수 내부에서 사용하기 위해 임시 변수에 저장
		const currentIndex = activeInputIndex;

		geocoder.coord2Address(clickedPosition.getLng(), clickedPosition.getLat(), function(result, status) {
			if (status === kakao.maps.services.Status.OK) {
				const address = result[0].road_address ? result[0].road_address.address_name : result[0].address.address_name;
				
				console.log('Geocoder Success for index: ' + currentIndex);

				// 1. UI 업데이트: 자동 주소란에 텍스트 채우기
				$('.input-group[data-index="' + currentIndex + '"] .auto-address').text(address);
				
				// 2. 데이터 업데이트: waypoints 배열에 좌표 정보 저장 (place는 최종 등록 시 저장)
				if (!waypoints[currentIndex]) {
                    waypoints[currentIndex] = {}; // 객체가 없으면 생성
                }
				waypoints[currentIndex].lat = clickedPosition.getLat();
				waypoints[currentIndex].lng = clickedPosition.getLng();

				// 3. 마커 업데이트: 해당 위치에 마커를 추가하거나 이동
				addOrUpdateMarker(currentIndex, clickedPosition);
				
				console.log('현재 waypoints 데이터:', waypoints);

				// 안내 메시지 원상 복구
				//$('#guide-message').text('입력란을 클릭하여 활성화한 후, 지도에서 원하는 위치를 클릭하세요.').css('color', 'black');
				// 활성화된 인덱스를 초기화하여, 같은 곳을 연속으로 찍는 것을 방지
				activeInputIndex = null;
				console.log('Map Click End: activeInputIndex set to null');
			}
		});
		

		/*
		클릭한 위치에 마커 생성
		const marker = new kakao.maps.Marker({
			position: event.latLng
		});
		
		marker.setMap(map);
		mlist.push(marker); //생성된 마커를 배열에 추가

		//클릭한 좌표를 임시 변수에 저장
		currentClickedCoords = event.latLng;
		$('#spotName').focus();
		
		클릭한 좌표를 hidden태그에 넣기 -> 배열로 인해 불필요해짐
		$('input[name=lat]').val(event.latLng.getLat());
		$('input[name=lng]').val(event.latLng.getLng());
		*/
	});


	// [3] 지점(경유지) 추가 버튼 클릭 이벤트 - waypoints 배열에 데이터 추가
	$('#add-waypoint-btn').on('click', () => {
		if (waypoints.length >= 7) {
			// alert('경유지는 최대 7개까지 추가할 수 있습니다.');
			return;
		}
		const newIndex = waypoints.length - 1; // 도착지 바로 앞에 추가될 인덱스

		// 1. 데이터 업데이트: waypoints 배열에 경유지 공간(null) 추가
		waypoints.splice(newIndex, 0, null);
		markers.splice(newIndex, 0, null); //도착지 마커가 이동하는 현상 수정
		
		// 2. UI 업데이트: 새로운 경유지 입력 그룹 HTML 생성 및 삽입
		const newWaypointHTML = `
			<div class="input-group">
				<span>경유지</span>
				<div class="spot-details">
					<div class="auto-address">지도에서 선택하세요</div>
					<input type="text" class="custom-name-input" placeholder="장소의 별명을 입력하세요">
				</div>
				<button type="button" class="btn-action btn-remove" title="경유지 삭제">x</button>
			</div>
		`;
		//$('#waypoints-container').append(newWaypointHTML);
		$(newWaypointHTML).insertBefore('#end-point-group');

		// 3. 인덱스 및 라벨 재정렬
		updateIndexesAndLabels();

		/* 
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
		*/
	});

	// [4] 경유지 삭제(X) 버튼 클릭 시 (이벤트 위임)
	$(document).on('click', '.btn-remove', function() {
		const groupToRemove = $(this).closest('.input-group');
		const indexToRemove = groupToRemove.data('index');

		// 1. 데이터 업데이트
		waypoints.splice(indexToRemove, 1);
		
		// 2. 마커 업데이트
		if (markers[indexToRemove]) {
			markers[indexToRemove].setMap(null); // 지도에서 마커 제거
		}
		markers.splice(indexToRemove, 1); // 마커 배열에서도 제거

		// 3. UI 업데이트
		groupToRemove.remove();

		// 4. 인덱스 및 라벨 재정렬
		updateIndexesAndLabels();
	});



	// [5] 코스 등록 버튼 클릭 이벤트 -> 서버에 배열 데이터 전송
	$('#btnRegisterCourse').on('click', () => {
		//ajax 처리, courseregister.do에서...
		//alert();

		const courseName = $('#courseName').val();
		// 1. 최종 데이터 정리 및 유효성 검사
		const finalWaypoints = [];
		let hasError = false;
		
		$('#spot-groups-wrapper').find('.input-group').each(function() {
			const index = $(this).data('index');
			const customName = $(this).find('.custom-name-input').val();
			
			// 좌표가 선택되지 않았거나, 별명을 입력하지 않은 경우
			if (!waypoints[index] || !waypoints[index].lat || !customName) {
				hasError = true;
				return; // each 반복 중단
			}
			
			// waypoints의 place 속성에 최종 별명 저장
			waypoints[index].place = customName;
			finalWaypoints.push(waypoints[index]);
		});
		
		if (hasError || finalWaypoints.length < 2) {
			alert('모든 지점의 위치를 선택하고 별명을 입력해주세요.');
			return;
		}
		if (!courseName) {
			alert('코스 이름을 입력해주세요.');
			$('#courseName').focus();
			return;
		}

		// 2. 서버로 전송할 최종 데이터 객체 생성
		const courseData = {
			courseName: courseName,
			spots: finalWaypoints
		};
		console.log('서버로 전송할 최종 데이터:', JSON.stringify(courseData));

		/*
		// 출발, 도착지는 필수이므로 최소 2개 이상의 지점이 있어야 함
		// if (waypoints.length < 2) {
		// 	alert('출발지와 도착지는 필수 입력사항입니다.');
		// 	return;
		// }

		// const lat = $('#lat').val();
		// const lng = $('#lng').val();
		// const place = $('#place').val();
		
		// if((lat=='')||(lng=='')) {
		// 	alert('지도에 마커를 먼저 추가해주세요.');
		// 	return;
		// }
		*/
		
		$.ajax({
			type: 'post',
			url: '/alldayrun/course/courseregister.do',
			data: JSON.stringify(courseData),
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			success: function(result) {
				if (result.result === 'success') {
					alert('코스 등록이 완료되었습니다.');
					location.href = '/course/coursesearch.do'; // 성공 시 메인 페이지 등으로 이동
				} else {
					alert('코스 등록에 실패했습니다.');
				}
			},
			error: function(a,b,c){
				console.log(a,b,c);
				alert('서버와 통신 중 오류가 발생했습니다.');
			}
		});
		
	});

	// =======================
	// 3. 보조(Helper) 함수
	// =======================

	/**
	 * 지도에 마커를 추가하거나, 이미 있으면 위치를 업데이트하는 함수
	 * @ param {number} index - 마커와 연결된 waypoints 배열의 인덱스
	 * @ param {object} position - Kakao Maps의 LatLng 객체
	 */
	function addOrUpdateMarker(index, position) {
		if (markers[index]) {
			markers[index].setPosition(position);
		} else {
			const marker = new kakao.maps.Marker({
				position: position,
				map: map,
				draggable: true
			});
			// 2. 나중에 dragend 이벤트에서 마커의 index를 알 수 있도록, 마커 객체에 직접 index 정보를 저장
			marker.customIndex = index;

			kakao.maps.event.addListener(marker, 'dragend', function() {
				// 이벤트가 발생한 마커('this')의 새로운 위치를 가져옵니다.
				const newPosition = this.getPosition();
				
				// 마커에 저장해 둔 index를 가져옵니다.
				const markerIndex = this.customIndex;

				// A. waypoints 배열의 좌표 데이터를 새로운 위치로 업데이트합니다.
				waypoints[markerIndex].lat = newPosition.getLat();
				waypoints[markerIndex].lng = newPosition.getLng();

				// B. 변경된 좌표의 주소를 가져와 UI에 반영합니다.
				geocoder.coord2Address(newPosition.getLng(), newPosition.getLat(), function(result, status) {
					if (status === kakao.maps.services.Status.OK) {
						const newAddress = result[0].road_address ? result[0].road_address.address_name : result[0].address.address_name;
						$('.input-group[data-index="' + markerIndex + '"] .auto-address').text(newAddress);
					}
				});

				console.log('마커 드래그 발생. 업데이트된 waypoints:', waypoints);
			});

			//생성된 마커를 배열에 저장
			markers[index] = marker;
		}
	}
	
	/**
	 * 경유지 추가/삭제 시, 화면의 모든 지점 그룹의 data-index와 라벨을 순서대로 다시 설정하는 함수
	 */
	function updateIndexesAndLabels() {
		// ✅ 수정된 부분: .find()를 사용해 모든 하위의 .input-group을 올바르게 찾습니다.
		const $groups = $('#spot-groups-wrapper').find('.input-group');
		const totalGroups = $groups.length;
		let waypointCounter = 1;

		$groups.each(function(i) {
			const $currentGroup = $(this);
			const $labelSpan = $currentGroup.find('span');

			// 1. data-index를 순서대로 재설정
			$currentGroup.attr('data-index', i);

			// 2. 마커의 이름표(customIndex) 재설정
			if (markers[i]) {
				markers[i].customIndex = i;
			}

			// 3. 라벨 텍스트를 순서에 맞게 재설정
			if (i === 0) {
				// 첫 번째 요소는 무조건 '출발지'
				$labelSpan.text('출발지');
			} else if (i === totalGroups - 1) {
				// 마지막 요소는 무조건 '도착지'
				$labelSpan.text('도착지');
			} else {
				// 그 외 중간 요소들은 '경유지'
				$labelSpan.text('경유지' + waypointCounter);
				waypointCounter++;
			}
		});
		
		// 전체 지점 수가 7개(출발1+경유지5+도착1) 이상이면 추가 버튼을 숨기고, 아니면 표시한다.
    	$('#add-waypoint-btn').toggle(totalGroups < 7);

		console.log('인덱스 재정렬 후 waypoints 배열:', waypoints);
	}

});