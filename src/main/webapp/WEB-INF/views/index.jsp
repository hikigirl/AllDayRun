<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/asset/css/index.css">
</head>
<body>
 
	<div id="layout">
		<!-- 헤더 -->
		<%@include file="/WEB-INF/views/inc/header.jsp"%>

		<!-- 사이드바 + 메인 -->
		<div class="content-wrapper">
			<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
			<!-- 메인 컨텐츠 영역 -->
			<div class="main-content" id="main-index">
				<!-- 여기 태그 내부에 본인 담당 페이지 html를 작성 -->
				<section class="main-content" id="index-maincontent-wrapper">
					<!-- <h2>오늘의 날씨 ☀️</h2> -->
					<section class="dashboard">
						<section class="weather-panel">
							<div class="weather-bg">
								<div class="overlay">

									<!-- 🟡 러닝지수 -->
									<h2 class="running-index">
										오늘의 러닝지수: <span id="running-value" class="status">-</span> <span
											id="running-reason" class="reason"></span>
									</h2>

									<!-- 🟡 현재 온도 -->
									<h3>
										현재 온도는 <span class="temp">--</span>℃ 입니다.
									</h3>

									<!-- 🟡 시간대별 기온 그래프 -->
									<div class="temp-graph">
										<canvas id="tempChart"></canvas>
									</div>

									<!-- 🟡 이번주 날씨 요약 -->
									<div class="weekly-weather"></div>

									<!-- 🟡 추천 복장 -->
									<div class="recommend-outfit">
										<h3>👕 오늘의 추천 러닝복장</h3>
										<ul id="outfit-list">
											<!-- JS에서 동적으로 채움 -->
										</ul>
									</div>
								</div>
							</div>
						</section>

						<!------------------------- 팁박스 ------------------------->
						<section class="tip-panel">
							<div class="tip-bg">
								<div class="tip-overlay">
									<h2>러너들을 위한 오늘의 팁 💡</h2>
									<ul>
										<!-- js에서 로드 -->
									</ul>
								</div>
							</div>
						</section>

						<!-- 구분선 -->
						<!-- <div class="divider">
							<hr>
						</div> -->

						<!-- 🏞 추천 코스 패널 -->
						<!--<div class="course-panel">
							<h2>오늘의 추천 코스 🏃‍♂️</h2> -->

							<!-- ✅ [DB 연동 예정 구간]
	       나중에 여기에 DB에서 코스 목록을 불러와서 출력할 예정입니다.
	       예시)
	       <c:forEach var="course" items="${courseList}">
	         <li>
	           <a href="${pageContext.request.contextPath}/course/view.do?no=${course.id}">
	             ${course.name}
	           </a>
	         </li>
	       </c:forEach>
	  -->
						<!--	<ul class="course-list">
								<li><a href="#">한강 러닝 코스</a></li>
								<li><a href="#">올림픽공원 코스</a></li>
								<li><a href="#">서울숲 둘레길</a></li>
							</ul>
						</div> -->
						<!-- ✅ 러너들을 위한 오늘의 팁 (하루 3개 랜덤 고정) -->
						<script>
document.addEventListener("DOMContentLoaded", () => {

  // ① 러닝 팁 목록 (원하는 만큼 추가 가능)
  const tips = [
  "러닝 전 5~10분 스트레칭으로 근육을 풀어주세요.",
  "물을 조금씩 자주 마시며 탈수를 예방하세요.",
  "착지 시 무릎 충격을 줄이려면 발뒤꿈치부터 닿도록 하세요.",
  "러닝화는 평균 600km 후 교체하는 게 좋아요.",
  "비 오는 날엔 미끄럼 방지 러닝화를 신으세요.",
  "운동 후 쿨다운 스트레칭으로 근육 회복을 도와요.",
  "공복 러닝은 체지방 연소엔 좋지만, 저혈당 주의!",
  "어깨는 긴장하지 말고 팔을 자연스럽게 흔드세요.",
  "조깅으로 몸을 덥히고 본 운동에 들어가세요.",
  "러닝 후 충분한 수면은 회복의 핵심이에요.",
  "러닝 전 탄수화물 섭취로 에너지를 보충하세요.",
  "햇빛이 강한 날엔 자외선 차단제를 꼭 바르세요.",
  "호흡은 일정하게, 얕지 않게 유지하세요.",
  "심박수를 체크하면서 달리면 효율이 올라가요.",
  "러닝 후 단백질 섭취로 근육 회복을 돕세요.",
  "무리한 속도보다 일정한 페이스가 중요합니다.",
  "새 신발은 러닝 전 짧게 길들여 주세요.",
  "시선은 정면, 어깨는 릴랙스 상태로 유지하세요.",
  "러닝 전후로 체중을 재서 수분량을 체크하세요.",
  "통증이 생기면 즉시 멈추세요.",
  "바닥을 너무 세게 차지 말고 부드럽게 착지하세요.",
  "러닝화 끈은 꼭 맞게 조여야 발의 피로가 줄어요.",
  "기온이 낮을 땐 천천히 몸을 덥히세요.",
  "러닝 전 화장실은 꼭 다녀오세요.",
  "러닝 후 바로 앉기보다 가벼운 걷기로 회복하세요.",
  "추운 날엔 얇은 옷 여러 겹이 좋습니다.",
  "러닝 전날엔 과음과 기름진 음식은 피하세요.",
  "피로가 심하면 하루 쉬는 것도 훈련의 일부예요.",
  "러닝 일지를 써서 컨디션을 기록해보세요.",
  "달리기 전날엔 수면을 충분히 취하세요.",
  "아침 러닝은 공기가 맑고 집중력 향상에 좋아요.",
  "러닝 중 코로 들이마시고 입으로 내쉬는 게 좋아요.",
  "러닝 후 발목 스트레칭을 빼먹지 마세요.",
  "페이스는 GPS보다 몸의 느낌을 믿으세요.",
  "러닝 중 통증보다 불편함이 크면 즉시 속도를 줄이세요.",
  "러닝화 안쪽이 닳으면 교체 시기입니다.",
  "비 오는 날엔 모자와 방수 윈드브레이커를 착용하세요.",
  "러닝 직후 찬물 샤워는 근육 염증을 줄여줍니다.",
  "러닝 후 30분 이내에 단백질을 섭취하세요.",
  "배가 너무 부르면 러닝 전에 소화 시간을 두세요.",
  "달릴 때 팔꿈치는 90도 각도로 유지하세요.",
  "무릎 통증이 지속되면 폼롤러로 마사지하세요.",
  "너무 얇은 양말은 물집을 유발할 수 있어요.",
  "러닝 중 자세가 무너지면 짧게 걷는 것도 괜찮아요.",
  "러닝화 밑창의 마모 상태를 수시로 확인하세요.",
  "꾸준한 속도보다 꾸준한 습관이 더 중요해요.",
  "러닝 직후엔 급격한 스트레칭보다 부드럽게 풀어주세요.",
  "러닝 중 날씨 변화에 대비해 가벼운 겉옷을 챙기세요.",
  "러닝 후엔 다리 높이 올리기 스트레칭이 좋아요.",
  "러닝 중 어깨를 너무 올리지 말고 편하게 유지하세요.",
  "장시간 러닝 전엔 소금 섭취도 중요해요.",
  "가벼운 러닝이라도 워밍업은 필수입니다.",
  "러닝할 땐 자신만의 리듬을 찾아보세요.",
  "햇빛이 강하면 선글라스로 눈 보호를 해주세요.",
  "러닝은 심폐 지구력 향상에 가장 좋은 운동이에요.",
  "러닝 전에 심박수를 체크하면 몸 상태를 알 수 있어요.",
  "새로운 코스를 달리면 동기부여가 됩니다.",
  "러닝 전후로 수분 섭취를 잊지 마세요.",
  "피곤할 땐 거리보다 시간을 기준으로 달리세요.",
  "러닝 중에 손을 꽉 쥐면 어깨가 긴장됩니다.",
  "너무 추운 날엔 코로만 숨 쉬어 폐를 보호하세요.",
  "러닝 후 스트레칭은 부상 방지의 기본이에요.",
  "달리기 전 1~2시간 전엔 가벼운 식사를 하세요.",
  "음악은 페이스 조절에 도움이 됩니다.",
  "러닝화는 발볼에 꼭 맞게 고르세요.",
  "러닝 중 복부에 통증이 있으면 호흡을 깊게 하세요.",
  "일주일에 하루는 휴식일을 두세요.",
  "러닝 시 핸드폰은 가볍게, 최소한으로 챙기세요.",
  "러닝 중 허리를 꼿꼿이 세우세요.",
  "겨울엔 손끝 보온을 위해 장갑을 착용하세요.",
  "러닝 중 호흡이 너무 빠르면 속도를 낮추세요.",
  "발뒤꿈치보다 중간착지가 무릎 부담을 줄여요.",
  "러닝은 체중보다 체력 향상에 집중해야 해요.",
  "습도가 높을 땐 페이스를 조절하세요.",
  "러닝 전 스트레칭은 반동 없이 천천히 하세요.",
  "러닝 전 충분한 수분 섭취는 필수입니다.",
  "러닝 중 피로를 느끼면 자세를 다시 점검하세요.",
  "바람이 강할 땐 상체를 살짝 숙이세요.",
  "러닝화 쿠션은 발 충격 완화의 핵심이에요.",
  "러닝 중 손목 시계는 너무 꽉 차지 않게 하세요.",
  "비 오는 날엔 마찰 방지를 위해 바셀린을 발라주세요.",
  "러닝 전후로 스트레칭 순서를 유지하세요.",
  "장시간 러닝엔 에너지젤을 준비하세요.",
  "땀을 많이 흘리면 염분 섭취도 잊지 마세요.",
  "새벽 러닝 시 반사 밴드 착용은 필수예요.",
  "러닝 중 음악 대신 호흡에 집중해보세요.",
  "꾸준함이 러닝 실력 향상의 비결입니다.",
  "러닝 후 즉시 앉지 말고 5분간 천천히 걸으세요.",
  "러닝 후 차가운 물보다 미지근한 물로 샤워하세요.",
  "달리기 후 다리 근육을 폼롤러로 풀어주세요.",
  "러닝 중 너무 덥다면 물을 머리에 살짝 뿌리세요.",
  "러닝 중 발이 저리면 신발 끈을 다시 조절하세요.",
  "러닝 중 시선은 15m 앞을 보는 게 좋습니다.",
  "러닝화 사이즈는 평소보다 0.5~1cm 크게 선택하세요.",
  "러닝 후에는 반드시 수분 보충을 해주세요.",
  "러닝 중 불편한 부위가 있다면 즉시 점검하세요.",
  "러닝 중 소리를 내며 호흡하면 리듬 유지에 좋아요.",
  "러닝 전 커피 한 잔은 집중력 향상에 도움돼요.",
  "러닝은 완벽보다 꾸준함이 중요합니다.",
  "러닝 중 미소를 짓는 것도 페이스 유지에 도움이 돼요."
];

  // ② 오늘 날짜를 시드로 고정
  const today = new Date();
  const seed = today.getFullYear() * 10000 + (today.getMonth() + 1) * 100 + today.getDate();

  // ③ 날짜 기반 고정 랜덤 생성기
  function seededRandom(seed) {
    const x = Math.sin(seed) * 10000;
    return x - Math.floor(x);
  }

  // ④ 팁 배열 섞기
  const shuffled = [...tips];
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(seededRandom(seed + i) * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
  }

  // ⑤ 상위 3개만 선택
  const todayTips = shuffled.slice(0, 3);

  // ⑥ HTML에 출력
  const tipList = document.querySelector(".tip-panel ul");
  if (tipList) {
    tipList.innerHTML = "";
    todayTips.forEach(tip => {
      const li = document.createElement("li");
      li.textContent = tip;
      tipList.appendChild(li);
    });
  }
});
</script>
					</section>
			</div>

		</div>
	</div>


	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>


	<!-- Chart.js 라이브러리 (CDN) -->
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<!-- main.js 불러오기 -->
	<script src="${pageContext.request.contextPath}/asset/js/main.js"></script>

	<!-- ✅ [날씨 스크립트: One Call 3.0 + Geolocation] -->
	<script>
document.addEventListener("DOMContentLoaded", () => {

  // ① 현재 위치 가져오기
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(onSuccess, onError);
  } else {
    alert("이 브라우저는 위치 정보를 지원하지 않습니다.");
    fetchWeather(37.5729, 126.9793); // 서울 기본 좌표
  }

  function onSuccess(pos) {
    const lat = pos.coords.latitude;
    const lon = pos.coords.longitude;
    fetchWeather(lat, lon);
  }

  function onError() {
    alert("위치를 불러올 수 없어 기본 위치(서울 종로)로 표시합니다.");
    fetchWeather(37.5729, 126.9793);
  }

  /* ✅ [추가] 위치명 가져오기 (Reverse Geocoding) */
  async function fetchLocationName(lat, lon) {
    try {
      const res = await fetch(`https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lon}&format=json&addressdetails=1`);
      const data = await res.json();

      // 시/구 단위 주소 추출
      const city = data.address.city || data.address.state || data.address.region || "";
      const district = data.address.suburb || data.address.county || data.address.town || data.address.village || "";

      const locationText = city && district ? `${city} ${district}` : (city || "위치 정보 없음");
      document.getElementById("user-location").textContent = `📍 ${locationText}`;
    } catch (err) {
      console.error("❌ 위치 정보 불러오기 실패:", err);
      document.getElementById("user-location").textContent = "📍 위치 확인 실패";
    }
  }

  // ② 서버 프록시(/weather.do) 호출
  async function fetchWeather(lat, lon) {
    try {
      /* ✅ [추가] 위치 정보 표시 */
      fetchLocationName(lat, lon);

      // ✅ 템플릿 리터럴 → 문자열 더하기 방식 (JSP 충돌 방지)
      const res = await fetch(location.origin + getContextPath() + "/weather.do?lat=" + lat + "&lon=" + lon);
      if (!res.ok) throw new Error("서버 응답 오류: " + res.status);
      const data = await res.json();

      console.log("🌤 One Call 3.0 응답:", data);

      // ✅ 현재 기온 표시
      if (data.current && data.current.temp != null) {
        document.querySelector(".temp").textContent = Math.round(data.current.temp);
      } else {
        document.querySelector(".temp").textContent = "--";
      }
      
   // ✅ 러닝지수 계산
      if (data.current) {
        const temp = data.current.temp;
        const weather = data.current.weather[0].main; // 예: Clear, Rain, Clouds 등
        let runningIndex = "보통";
        let colorClass = "normal";
        let reason = "";

     // 🌧 날씨 기반 판정
        if (["Rain", "Snow", "Thunderstorm"].includes(weather)) {
          runningIndex = "나쁨";
          colorClass = "bad";
          if (weather === "Rain") reason = "비가 오는 날입니다 🌧";
          else if (weather === "Snow") reason = "눈이 오는 날입니다 ❄️";
          else reason = "날씨가 좋지 않습니다 ⚡";
        } else if (weather === "Clear") {
          runningIndex = "좋음";
          colorClass = "good";
          reason = "맑고 러닝하기 좋은 날씨에요 ☀️";
        } else if (weather === "Clouds") {
          runningIndex = "보통";
          colorClass = "normal";
          reason = "구름이 조금 있지만 괜찮아요 ☁️";
        } else {
          runningIndex = "보통";
          colorClass = "normal";
          reason = "기온과 날씨를 확인하세요 🌤";
        }

        // 🌡 온도 기반 조정
        if (temp < 5) {
          runningIndex = "나쁨";
          colorClass = "bad";
          reason = "너무 추워서 러닝하기 어려워요 🥶";
        } else if (temp > 30) {
          runningIndex = "나쁨";
          colorClass = "bad";
          reason = "너무 더워서 러닝하기 힘들어요 🥵";
        } else if ((temp >= 5 && temp < 10) || (temp >= 25 && temp <= 30)) {
          if (runningIndex !== "나쁨") {
            runningIndex = "보통";
            colorClass = "normal";
            reason = "기온이 조금 불편할 수 있어요 😐";
          }
        }

        // 표시 업데이트
        const valueEl = document.getElementById("running-value");
        const reasonEl = document.getElementById("running-reason");
		
        valueEl.textContent = runningIndex;
        valueEl.className = "status " + colorClass;
        reasonEl.textContent = reason;
      }

      // ✅ 시간대별 온도 데이터 (오늘 0시 ~ 21시 / 3시간 간격 / 오전오후 표기)
      if (data.hourly && data.hourly.length > 0) {
        const tzOffset = (typeof data.timezone_offset === "number") ? data.timezone_offset : 0;

        // 현지 시각으로 변환한 배열 생성
        const adjusted = data.hourly.map(h => {
          const local = new Date((h.dt + tzOffset) * 1000);
          return { 
            hour: local.getUTCHours(),
            date: local.getUTCDate(),
            temp: Math.round(h.temp)
          };
        });

        // 오늘 날짜 구하기
        const now = new Date();
        const today = new Date(now.getTime() + tzOffset * 1000);
        const todayDate = today.getUTCDate();

        // 오늘 0시~21시만 필터링
        const todayData = adjusted.filter(h => h.date === todayDate && h.hour % 3 === 0 && h.hour <= 21);

        // 오전/오후 표시 라벨
        const labels = todayData.map(h => {
          const ampm = h.hour >= 12 ? "오후" : "오전";
          const hour12 = (h.hour % 12) || 12;
          return ampm + " " + hour12 + "시";
        });

        const temps = todayData.map(h => h.temp);

        // ✅ 차트로 데이터 전달
        const payload = { labels, temps, rawData: data };
        window.weatherChartData = payload;
        window.dispatchEvent(new CustomEvent("weatherDataReady", { detail: payload }));
      }
      
      /* ✅ 2️⃣ 추천 복장 갱신 */
      updateOutfit(data.current.temp, data.current.weather[0].main);

      /* ✅ 추천 복장 표시 함수 */
      function updateOutfit(temp, weather) {
        const outfitList = document.getElementById("outfit-list");
        if (!outfitList) return;
        outfitList.innerHTML = ""; // 기존 항목 제거

        let outfits = [];

        // 🌡 기온 기반 기본 복장
        if (temp < 0) {
		  outfits = [
		    "방풍 하드쉘 재킷",
		    "기모 러닝 타이즈",
		    "러닝 장갑 & 비니",
		    "보온 기능 러닝 슈즈"
		  ];
		} else if (temp < 5) {
		  outfits = [
		    "히트텍 이너웨어 + 러닝 자켓",
		    "기모 타이즈",
		    "보온 캡 또는 이어워머"
		  ];
		} else if (temp < 10) {
		  outfits = [
		    "드라이핏 긴팔 러닝티",
		    "경량 윈드브레이커",
		    "컴프레션 타이즈"
		  ];
		} else if (temp < 20) {
		  outfits = [
		    "흡습속건 반팔 러닝티",
		    "하프 타이즈 또는 러닝 반바지",
		    "가벼운 모자 또는 헤어밴드"
		  ];
		} else if (temp < 28) {
		  outfits = [
		    "통기성 우수한 슬리브리스 또는 반팔",
		    "러닝 반바지",
		    "쿨링 소재 모자"
		  ];
		} else {
		  outfits = [
		    "냉감 슬리브리스",
		    "초경량 러닝 쇼츠",
		    "UV 차단 기능 모자 & 쿨토시"
		  ];
		}

        // 🌦 날씨 기반 추가 조정
        if (weather === "Rain") {
          outfits.push("방수 윈드브레이커", "방수 캡");
        } else if (weather === "Snow") {
          outfits.push("미끄럼 방지 러닝화", "보온 양말");
        } else if (weather === "Thunderstorm") {
          outfits = ["⚠️ 번개 위험 — 러닝은 피하세요!"];
        }

        // HTML 출력
        outfits.forEach(item => {
          const li = document.createElement("li");
          li.textContent = item;
          outfitList.appendChild(li);
        });
      }

    } catch (err) {
      console.error("❌ 날씨 API 오류:", err);
    }
  }

  // ✅ JSP 컨텍스트 경로 자동 인식 함수
  function getContextPath() {
    const path = window.location.pathname;
    const idx = path.indexOf("/", 1);
    return idx === -1 ? "" : path.substring(0, idx);
  }
  
  /* ✅ 배경 변경 함수 */
  function updateWeatherBackground(weather) {
    const bg = document.querySelector(".weather-bg");
    if (!bg) return;

    const weatherImages = {
      Clear: "sunny.jpg",
      Clouds: "cloudy.jpg",
      Rain: "rainy.jpg",
      Snow: "snowy.jpg",
      Thunderstorm: "thunder.jpg",
      Drizzle: "drizzle.jpg",
      Mist: "foggy.jpg",
      Fog: "foggy.jpg"
    };

    const imageName = weatherImages[weather] || "default.jpg";

    // ✅ JSP에 맞게 이미지 경로 설정
    bg.style.background = `url('${pageContext.request.contextPath}/asset/images/${imageName}') no-repeat center/cover`;
  }
});
</script>
	<!-- ✅ [끝] -->

</body>
</html>