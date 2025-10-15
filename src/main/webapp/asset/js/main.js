document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".menu-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      // 클릭 시 active 토글
      btn.classList.toggle("active");

      // 바로 다음에 있는 submenu 열고 닫기
      const submenu = btn.nextElementSibling;
      if (submenu) {
        submenu.style.display = submenu.style.display === "block" ? "none" : "block";
      }
    });
  });
});

// 시간대별 기온 그래프 (예시 데이터)
document.addEventListener("DOMContentLoaded", () => {
  const ctx = document.getElementById('tempChart');
  if (ctx) {
    new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['06시', '09시', '12시', '15시', '18시', '21시'],
        datasets: [{
          label: '기온 (°C)',
		  /*나중에 API 붙일 때는
		  data: [18,21,24,...] 대신
		  response.list.map(x => x.temp) 같은 방식*/
          data: [15, 21, 24, 26, 23, 20],
          borderColor: '#ffec8b',
          backgroundColor: 'rgba(255, 236, 139, 0.3)',
          tension: 0.4,
          fill: true
        }]
      },
      options: {
		responsive: true,      // ✅ 반응형 활성화
		maintainAspectRatio: false, // ✅ 비율 고정 끄기
        plugins: { legend: { display: false } },
        scales: {
          y: { ticks: { color: '#fff' }, grid: { color: 'rgba(255,255,255,0.2)' } },
          x: { ticks: { color: '#fff' }, grid: { display: false } }
        }
      }
    });
  }
});