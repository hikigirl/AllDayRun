$(document).ready(function() {

	// ===================================
	// 1. 지도 생성 및 전역 변수 초기화
	// ===================================
	var container = document.getElementById('map');
	var options = {
		center: new kakao.maps.LatLng(37.51080190682596, 126.99716104149441),
		level: 7
	};
	var map = new kakao.maps.Map(container, options);
	const geocoder = new kakao.maps.services.Geocoder();

	let waypoints = [null, null];
	const markers = [null, null];
	let activeInputIndex = null;
	
	let coursePolyline = null; //경로 선을 저장할 변수 추가

	// ===================================
	// 2. 이벤트 핸들러
	// ===================================

	// [1] 입력 상자 클릭(focus) 시, 해당 상자를 '활성화'
	$(document).on('focus', '.custom-name-input', function() {
		activeInputIndex = $(this).closest('.input-group').data('index');
		console.log('활성화된 입력상자 index: ' + activeInputIndex);
		$('#guide-message').text('지도에서 원하는 위치를 클릭하세요.').css('color', '#a20759');
	});

	// [2] 지도 클릭 이벤트 (mousedown 사용)
	kakao.maps.event.addListener(map, 'click', event => {
		if (activeInputIndex === null) {
			console.log('Map Click Ignored: activeInputIndex is null');
			return;
		}

		const clickedPosition = event.latLng;
		const currentIndex = activeInputIndex;

		geocoder.coord2Address(clickedPosition.getLng(), clickedPosition.getLat(), function(result, status) {
			if (status === kakao.maps.services.Status.OK) {
				const address = result[0].road_address ? result[0].road_address.address_name : result[0].address.address_name;
				
				// 1. UI 업데이트
				$('.input-group[data-index="' + currentIndex + '"] .auto-address').text(address);

				// 2. 데이터 업데이트
				if (!waypoints[currentIndex]) {
					waypoints[currentIndex] = {};
				}
				waypoints[currentIndex].lat = clickedPosition.getLat();
				waypoints[currentIndex].lng = clickedPosition.getLng();
				
				// 💡 추가된 부분: 지도 클릭 시점에도 현재 입력된 별명을 즉시 동기화
				const currentCustomName = $('.input-group[data-index="' + currentIndex + '"] .custom-name-input').val();
				waypoints[currentIndex].place = currentCustomName;

				// 3. 마커 업데이트
				addOrUpdateMarker(currentIndex, clickedPosition);
				drawCoursePolyline(); // ✅ 추가
				console.log('지도 클릭 후 waypoints 데이터:', waypoints);
			}
		});
	});

	// 💡 [3] 새로운 핵심 로직: 별명 입력 시 실시간으로 waypoints 배열에 데이터 동기화
	$(document).on('input', '.custom-name-input', function() {
		const index = $(this).closest('.input-group').data('index');
		const customName = $(this).val();

		if (waypoints[index]) {
			waypoints[index].place = customName;
		} else {
			// 아직 지도 클릭 전이면, place 속성만이라도 초기화
			waypoints[index] = { place: customName };
		}
		//console.log('별명 입력 중... 현재 waypoints:', waypoints);
	});


	// [4] 경유지 추가 버튼 클릭 이벤트
	$('#add-waypoint-btn').on('click', () => {
		if (waypoints.length >= 7) return;
		
		const newIndex = waypoints.length - 1;
		waypoints.splice(newIndex, 0, null);
		markers.splice(newIndex, 0, null);
		
		const newWaypointHTML = `
			<div class="input-group">
				<span>경유지</span>
				<div class="spot-details">
					<div class="auto-address">지도에서 장소를 선택하세요.</div>
					<input type="text" class="custom-name-input" placeholder="장소의 별명을 입력하세요." maxlength="25">
				</div>
				<button type="button" class="btn-action btn-remove" title="경유지 삭제"><img src="${contextPath}/asset/buttonimg/remove_btn.png" alt="경유지 삭제"></button>
			</div>
		`;
		$(newWaypointHTML).insertBefore('#end-point-group');
		updateIndexesAndLabels();
	});

	// [5] 경유지 삭제 버튼 클릭 시
	$(document).on('click', '.btn-remove', function() {
		const groupToRemove = $(this).closest('.input-group');
		const indexToRemove = groupToRemove.data('index');

		// 1. 삭제할 마커를 변수에 먼저 저장
		const markerToRemove = markers[indexToRemove];
		
		waypoints.splice(indexToRemove, 1);
		markers.splice(indexToRemove, 1);
		
		if (markerToRemove) {
			markerToRemove.setMap(null);
		}
		groupToRemove.remove();
		updateIndexesAndLabels();
		drawCoursePolyline(); // ✅ 추가
		activeInputIndex = null;
	});

	// [6] 코스 등록 버튼 클릭 이벤트
	$('#btnRegisterCourse').on('click', () => {
		const courseName = $('#courseName').val();
		
		// ✅ 수정된 부분: 더 이상 .each() 루프 안에서 데이터를 조합할 필요 없음
		// waypoints 배열은 이미 최신 상태이므로, 유효성 검사만 수행
		const finalWaypoints = waypoints.filter(wp => wp && wp.lat && wp.lng && wp.place);
		
		if (finalWaypoints.length !== waypoints.length || finalWaypoints.length < 2) {
			alert('모든 지점의 위치를 선택하고 별명을 입력해주세요.');
			return;
		}
		if (!courseName) {
			alert('코스 이름을 입력해주세요.');
			$('#courseName').focus();
			return;
		}

		const courseData = {
			courseName: courseName,
			spots: finalWaypoints
		};
		console.log('서버로 전송할 최종 데이터:', JSON.stringify(courseData));

		$.ajax({
			type: 'post',
			url: contextPath + '/course/courseregister.do',
			data: JSON.stringify(courseData),
			contentType: 'application/json; charset=utf-8',
			dataType: 'json',
			success: function(result) {
				if (result.result === 'success') {
					alert('코스 등록 요청이 완료되었습니다.');
					location.href = contextPath + '/course/coursemain.do'; // 성공 시 페이지 이동
				} else {
					alert('코스 등록에 실패했습니다.');
				}
			},
			error: function(a, b, c) {
				console.log(a, b, c);
				alert('서버와 통신 중 오류가 발생했습니다.');
			}
		});
	});

	// =======================
	// 3. 보조(Helper) 함수
	// =======================
	function addOrUpdateMarker(index, position) {
		if (markers[index]) {
			markers[index].setPosition(position);
		} else {
			const marker = new kakao.maps.Marker({
				position: position,
				map: map,
				draggable: true
			});
			marker.customIndex = index;

			kakao.maps.event.addListener(marker, 'dragend', function() {
				const newPosition = this.getPosition();
				const markerIndex = this.customIndex;

				waypoints[markerIndex].lat = newPosition.getLat();
				waypoints[markerIndex].lng = newPosition.getLng();

				geocoder.coord2Address(newPosition.getLng(), newPosition.getLat(), function(result, status) {
					if (status === kakao.maps.services.Status.OK) {
						const newAddress = result[0].road_address ? result[0].road_address.address_name : result[0].address.address_name;
						$('.input-group[data-index="' + markerIndex + '"] .auto-address').text(newAddress);
					}
				});
				console.log('마커 드래그 후 waypoints:', waypoints);
				drawCoursePolyline(); // ✅ 추가
			});
			markers[index] = marker;
		}
	}
	
	function updateIndexesAndLabels() {
		const $groups = $('#spot-groups-wrapper').find('.input-group');
		const totalGroups = $groups.length;
		let waypointCounter = 1;

		$groups.each(function(i) {
			const $currentGroup = $(this);
			$currentGroup.attr('data-index', i);

			if (markers[i]) {
				markers[i].customIndex = i;
			}

			const $labelSpan = $currentGroup.find('span');
			if (i === 0) {
				$labelSpan.text('출발지');
			} else if (i === totalGroups - 1) {
				$labelSpan.text('도착지');
			} else {
				$labelSpan.text('경유지' + waypointCounter);
				waypointCounter++;
			}
		});
		
		$('#add-waypoint-btn').toggle(totalGroups < 7);
		console.log('인덱스 재정렬 후 waypoints 배열:', waypoints);
	}
	/**
     * ✅ 지도 위에 코스 경로를 그리거나 업데이트하는 함수
     */
    function drawCoursePolyline() {
        // 1. 유효한 좌표(lat, lng)가 있는 지점들만 필터링하여 새로운 배열 생성
        const path = waypoints.filter(wp => wp && wp.lat && wp.lng)
                               .map(wp => new kakao.maps.LatLng(wp.lat, wp.lng));

        // 2. 이전에 그려진 선이 있다면 지도에서 제거
        if (coursePolyline) {
            coursePolyline.setMap(null);
        }

        // 3. 지점이 2개 이상일 때만 새로운 선을 그림
        if (path.length > 1) {
            // Polyline 객체 생성
            coursePolyline = new kakao.maps.Polyline({
                path: path, // 선을 구성하는 좌표 배열
                strokeWeight: 5, // 선의 두께
                strokeColor: '#FF0000', // 선의 색깔
                strokeOpacity: 0.7, // 선의 불투명도
                strokeStyle: 'solid' // 선의 스타일
            });

            // 지도에 선을 표시
            coursePolyline.setMap(map);
        }
    }
});