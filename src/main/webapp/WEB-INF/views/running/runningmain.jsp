<%-- <%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <%@include file="/WEB-INF/views/inc/asset.jsp"%> <!-- [설명 주석] 기존 에셋 include 유지 -->

  <title>러닝 메인</title>

  <!-- 러닝 전용 CSS: 요청한 경로/파일명 유지 -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/runningmain.css">

  <!-- ✅ 카카오맵 SDK 추가
       - [왜 추가?] 현재 위치를 지도에 표시해야 하므로 카카오 지도 SDK 필요
       - libraries=services: 지오코딩 등 확장을 대비(추후 러닝 경로/POI 활용 가능)
       - autoload=false: 스크립트 로드 완료 후 명시적으로 kakao.maps.load(...) 호출하기 위함(의존성 안전)
       - defer: DOM 파싱 뒤 로드 → 초기화 시점 제어 용이 -->
  <script
    src="//dapi.kakao.com/v2/maps/sdk.js?appkey=808d022c40d4346f120514672e36cd12&libraries=services&autoload=false"
    defer>
  </script>
</head>
<body>
  <div id="layout">
    <%@include file="/WEB-INF/views/inc/header.jsp"%> <!-- [설명 주석] 기존 헤더 유지 -->
    <div class="content-wrapper">
      <%@include file="/WEB-INF/views/inc/sidebar.jsp"%> <!-- [설명 주석] 기존 사이드바 유지 -->

      <!-- 🏃‍♂️ 러닝 메인 콘텐츠 -->
      <div class="main-content">
        <h1>🏃‍♂️ 러닝 메인 페이지</h1>

        <!-- ⬇️ 지도 컨테이너
             - [왜 추가?] 현재 위치를 시각화할 영역 -->
        <div id="map" class="map"></div>

        <!-- ⬇️ 타이머/컨트롤 UI
             - [왜 추가?] '운동시작/정지'에 따라 1초 단위 증가/정지 동작 구현 -->
        <div class="run-panel">
          <div class="timer" id="timer">00:00:00</div>
          <div class="controls">
            <button type="button" id="btnStart" class="btn primary">운동 시작</button>
            <button type="button" id="btnStop" class="btn danger">운동 정지</button>
          </div>
          <!-- [안내 주석] 브라우저 위치 권한 필요(HTTPS 또는 localhost 권장).
               개발 중이라면 http://localhost 에서 테스트 가능. 배포 시 https 권장. -->
          <p class="hint">※ 현재 위치 표시를 위해 브라우저 위치 권한을 허용하세요.</p>
        </div>
      </div>
    </div>
  </div>

  <script>
    // -------------------------------
    // 러닝 메인 페이지 스크립트
    // -------------------------------

    // [왜 전역에 두나?] 버튼 핸들러와 초기화 로직에서 공유해야 하므로 상단에 선언
    let map;                 // 카카오 지도 인스턴스
    let userMarker;          // 현재 위치 마커
    let accuracyCircle;      // 위치 정확도(반경) 표시 원
    let watchId = null;      // (확장 대비) 위치 추적 ID

    // 타이머 상태값
    let timerId = null;      // setInterval 핸들러 ID
    let baseStartTs = 0;     // "운동 시작" 기준 시작시각(밀리초)
    let carriedElapsed = 0;  // 누적 경과(정지 후 재시작을 위해 누적 저장)

    // [왜 함수로 분리?] 재사용/가독성 향상을 위해 기능별로 분리
    function formatHMS(ms) {
      // [왜 정밀도 유지?] 1초 단위 표시지만 내부는 밀리초로 관리해 일시정지/재시작 오차 최소화
      const totalSec = Math.floor(ms / 1000);
      const h = String(Math.floor(totalSec / 3600)).padStart(2, '0');
      const m = String(Math.floor((totalSec % 3600) / 60)).padStart(2, '0');
      const s = String(totalSec % 60).padStart(2, '0');
      return `${h}:${m}:${s}`;
    }

    function updateTimerDisplay(ms) {
      document.getElementById('timer').textContent = formatHMS(ms);
    }

    function startTimer() {
      // [가드] 이미 동작 중이면 무시
      if (timerId) return;

      // [왜 이렇게 계산?] 정지 후 재시작 시에도 누적 시간을 유지하기 위해,
      // 현재 시각 - (이미 지난 시간) = 이번 세션의 기준 시작시각
      baseStartTs = Date.now() - carriedElapsed;

      // [1초 단위 증가] setInterval을 1초로, 다만 표시 값은 실제 경과(ms) 기반으로 계산 → 드리프트 최소화
      timerId = setInterval(() => {
        const now = Date.now();
        const elapsed = now - baseStartTs;
        updateTimerDisplay(elapsed);
      }, 1000);
    }

    function stopTimer() {
      if (!timerId) return;
      clearInterval(timerId);
      timerId = null;

      // [왜 누적 저장?] 정지 시점의 경과 시간을 보존해 이후 재시작 시 이어서 증가하도록 함
      carriedElapsed = Date.now() - baseStartTs;
      updateTimerDisplay(carriedElapsed);
    }

    function resetTimer() {
      // [확장 대비] 필요 시 초기화할 수 있도록 별도 함수로 분리
      clearInterval(timerId);
      timerId = null;
      carriedElapsed = 0;
      baseStartTs = 0;
      updateTimerDisplay(0);
    }

    // -------------------------------
    // 지도 초기화
    // -------------------------------
    function initMap() {
      // [기본 중심점] 사용자 위치 권한 승인 전 임시 중심: 서울시청 좌표
      const defaultCenter = new kakao.maps.LatLng(37.5662952, 126.9779451);

      // [왜 옵션 분리?] 유지보수 편의성(레벨/컨트롤 등)
      const mapOptions = {
        center: defaultCenter,
        level: 5
      };

      map = new kakao.maps.Map(document.getElementById('map'), mapOptions);

      // [UX] 지도 컨트롤 추가(확대/축소)
      const zoomControl = new kakao.maps.ZoomControl();
      map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

      // ▶ 현재 위치 가져오기
      if ('geolocation' in navigator) {
        navigator.geolocation.getCurrentPosition(
          (pos) => {
            const { latitude, longitude, accuracy } = pos.coords;

            const latlng = new kakao.maps.LatLng(latitude, longitude);

            // [마커 생성] 사용자 현재 위치 시각화
            userMarker = new kakao.maps.Marker({
              position: latlng,
              map: map
            });

            // [정확도 원] accuracy(m)를 반경으로 표시 → 대략적인 신뢰 범위 제공
            const circleOptions = {
              map: map,
              center: latlng,
              radius: Math.max(accuracy, 30), // [왜 max?] 너무 작게 나오면 시각적으로 안 보여서 최소 30m
              strokeWeight: 1,
              strokeOpacity: 0.8,
              strokeColor: '#4a90e2', // [가독성] 정확도 원 외곽선 색(시인성 위해 지정)
              fillOpacity: 0.15,
              fillColor: '#4a90e2'
            };
            accuracyCircle = new kakao.maps.Circle(circleOptions);

            // [지도 이동] 현재 위치로 센터 이동
            map.setCenter(latlng);
          },
          (err) => {
            console.warn('위치 접근 거부/오류:', err);
            // [대응] 권한 거부 시 기본 중심 유지 + 안내는 상단 문구로 대체
          },
          { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 }
        );
      } else {
        console.warn('이 브라우저는 Geolocation을 지원하지 않습니다.');
      }
    }

    // -------------------------------
    // 초기 바인딩
    // -------------------------------
    document.addEventListener('DOMContentLoaded', () => {
      // [왜 kakao.maps.load 사용?] autoload=false로 로드했기 때문에 SDK 준비 완료 후 initMap 실행 보장
      if (window.kakao && kakao.maps && kakao.maps.load) {
        kakao.maps.load(initMap);
      } else {
        console.error('Kakao Maps SDK 로드 실패');
      }

      // 버튼 이벤트 바인딩
      document.getElementById('btnStart').addEventListener('click', startTimer); // [왜 추가?] 운동시작 버튼 클릭 시 타이머 시작
      document.getElementById('btnStop').addEventListener('click', stopTimer);   // [왜 추가?] 운동정지 버튼 클릭 시 타이머 정지

      // 초기 타이머 표시(00:00:00)
      updateTimerDisplay(0);
    });
  </script>
</body>
</html>
 --%>
 
 <%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <%@include file="/WEB-INF/views/inc/asset.jsp"%>

  <title>러닝 메인</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/runningmain.css">
</head>
<body>
  <div id="layout">
    <%@include file="/WEB-INF/views/inc/header.jsp"%>
    <div class="content-wrapper">
      <%@include file="/WEB-INF/views/inc/sidebar.jsp"%>

      <div class="main-content">
        <h1>🏃‍♂️ 러닝 메인 페이지</h1>

        <!-- 지도 자리 (아직 동작 X, 나중에 연결) -->
        <div id="map" class="map">지도 표시 예정</div>

        <!-- 타이머 영역 -->
        <div class="run-panel">
          <div class="timer" id="timer">00:00:00</div>
          <div class="controls">
            <button type="button" id="btnStart" class="btn primary">운동 시작</button>
            <button type="button" id="btnStop" class="btn danger" disabled>운동 정지</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script>
    // -------------------------------
    //  타이머 관련 전역 상태
    // -------------------------------
    let timerId = null;     // setInterval ID 저장
    let elapsedSec = 0;     // 경과 초 저장

    // -------------------------------
    //  공통 함수 (타이머/지도 둘 다 호출 가능)
    // -------------------------------
    function startRunning() {
      startTimer();  // 🔹 타이머 시작
      // 🚧 나중에 지도 시작 코드 들어올 자리 (예: startMapTracking();)
    }

    function stopRunning() {
      stopTimer();   // 🔹 타이머 정지
      // 🚧 나중에 지도 정지 코드 들어올 자리 (예: stopMapTracking();)
    }

    // -------------------------------
    //  타이머 전용 함수
    // -------------------------------
    function startTimer() {
      if (timerId) return; // 이미 실행 중이면 무시

      timerId = setInterval(() => {
        elapsedSec++;
        document.getElementById("timer").textContent = formatTime(elapsedSec);
      }, 1000);

      // 버튼 상태 변경
      document.getElementById("btnStart").disabled = true;
      document.getElementById("btnStop").disabled = false;
    }

    function stopTimer() {
      clearInterval(timerId);
      timerId = null;

      // 버튼 상태 변경
      document.getElementById("btnStart").disabled = false;
      document.getElementById("btnStop").disabled = true;
    }

    function formatTime(sec) {
      const h = String(Math.floor(sec / 3600)).padStart(2, "0");
      const m = String(Math.floor((sec % 3600) / 60)).padStart(2, "0");
      const s = String(sec % 60).padStart(2, "0");
      return `${h}:${m}:${s}`;
    }

    // -------------------------------
    //  이벤트 바인딩
    // -------------------------------
    document.addEventListener("DOMContentLoaded", () => {
      document.getElementById("btnStart").addEventListener("click", startRunning);
      document.getElementById("btnStop").addEventListener("click", stopRunning);
    });
  </script>
</body>
</html>
 