// main.js

document.addEventListener("DOMContentLoaded", () => {
  const canvas = document.getElementById('tempChart');
  if (!canvas) return;
  const ctx = canvas.getContext('2d');

  let chart;

  window.addEventListener("weatherDataReady", (e) => {
    const { labels, temps } = e.detail;
    if (chart) chart.destroy();

    // ✅ 최저/최고 찾기
    const minTemp = Math.min(...temps);
    const maxTemp = Math.max(...temps);
    const minIndex = temps.indexOf(minTemp);
    const maxIndex = temps.indexOf(maxTemp);

    chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels,
        datasets: [
          // 🟡 일반 기온 라인
          {
            label: '기온 (°C)',
            data: temps,
            borderColor: '#ffec8b',
            backgroundColor: 'rgba(255, 236, 139, 0.3)',
            tension: 0.4,
            fill: true,
            pointRadius: 4,
            pointBackgroundColor: '#ffec8b',
            pointBorderColor: '#fff',
            pointHoverRadius: 7
          },
          // 🔵 최저 기온 점 (하이라이트용)
          {
            label: '최저기온',
            data: temps.map((t, i) => (i === minIndex ? t : null)),
            borderColor: 'transparent',
            backgroundColor: '#3ab6ff',
            pointRadius: 8,
            pointHoverRadius: 10,
            pointStyle: 'circle'
          },
          // 🔴 최고 기온 점 (하이라이트용)
          {
            label: '최고기온',
            data: temps.map((t, i) => (i === maxIndex ? t : null)),
            borderColor: 'transparent',
            backgroundColor: '#ff5555',
            pointRadius: 8,
            pointHoverRadius: 10,
            pointStyle: 'circle'
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: true, // ✅ 범례 표시 활성화
            labels: {
              color: '#black',
              font: { size: 13 },
              boxWidth: 14,
              usePointStyle: true,
              padding: 15
            }
          },
          tooltip: {
            callbacks: {
              label: (context) => `${context.dataset.label}: ${context.parsed.y}°C`
            }
          }
        },
        scales: {
          y: {
            min: -5,
            max: 30,
            ticks: {
              color: '#fff',
              stepSize: 5,
            },
            grid: { color: 'rgba(255,255,255,0.2)' }
          },
          x: {
            ticks: {
              color: '#fff',
              maxRotation: 45,
              minRotation: 0,
              autoSkip: true,
              maxTicksLimit: 8
            },
            grid: { display: false }
          }
        },
        // ✅ 최저·최고 라벨 표시용 플러그인
        plugins: [{
          id: 'labelPoints',
          afterDatasetsDraw(chart) {
            const { ctx, scales: { x, y } } = chart;
            ctx.save();
            ctx.font = 'bold 13px Pretendard, sans-serif';
            ctx.textAlign = 'center';

            // 🔵 최저
            const xMin = x.getPixelForValue(minIndex);
            const yMin = y.getPixelForValue(minTemp);
            ctx.fillStyle = '#3ab6ff';
            ctx.fillText('최저', xMin, yMin - 12);

            // 🔴 최고
            const xMax = x.getPixelForValue(maxIndex);
            const yMax = y.getPixelForValue(maxTemp);
            ctx.fillStyle = '#ff5555';
            ctx.fillText('최고', xMax, yMax - 12);

            ctx.restore();
          }
        }]
      }
    });
  });
});

// ✅ 주간 날씨 (4일치) 표시
window.addEventListener("weatherDataReady", (e) => {
  const data = e.detail.rawData;
  const container = document.querySelector(".weekly-weather");
  if (!container || !data.daily) return;

  const weekData = data.daily.slice(0, 4);

  container.innerHTML = weekData.map(day => {
    const date = new Date(day.dt * 1000);
    const month = date.getMonth() + 1;
    const d = date.getDate();
    const weekday = ['일','월','화','수','목','금','토'][date.getDay()];

    // ✅ 최고 / 최저 기온
    const maxTemp = Math.round(day.temp.max);
    const minTemp = Math.round(day.temp.min);
    const icon = day.weather[0].icon;

    return `
      <div class="day-card">
        <div class="date">${month}월 ${d}일 (${weekday})</div>
        <div class="temp">${maxTemp}° / ${minTemp}°</div>
        <img src="https://openweathermap.org/img/wn/${icon}.png" alt="날씨아이콘" class="weather-icon"/>
      </div>
    `;
  }).join('');
});



