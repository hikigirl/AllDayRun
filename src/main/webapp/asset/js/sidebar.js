document.addEventListener('DOMContentLoaded', () => {

  // 화면이 데스크탑 크기인지 확인하는 함수 (801px 이상)
  const isDesktop = () => window.matchMedia('(min-width: 801px)').matches;

  document.querySelectorAll('.menu-btn').forEach(menuBtn => {
    
    const menuItem = menuBtn.parentElement;

    // 1. 데스크탑: 마우스를 올렸을 때
    menuItem.addEventListener('mouseover', () => {
      // 데스크탑 화면일 때만 실행
      if (isDesktop()) {
        menuBtn.classList.add('active'); // CSS 애니메이션을 위한 클래스만 추가
      }
    });

    // 2. 데스크탑: 마우스가 벗어났을 때
    menuItem.addEventListener('mouseout', () => {
      // 데스크탑 화면일 때만 실행
      if (isDesktop()) {
        menuBtn.classList.remove('active'); // CSS 애니메이션을 위한 클래스만 제거
      }
    });

    // 3. 클릭 이벤트 (데스크탑과 모바일에서 다르게 동작)
    menuBtn.addEventListener('click', (event) => {
      
      // 데스크탑 화면일 때: 페이지 이동
      if (isDesktop()) {
        const menuText = menuBtn.textContent.trim();
        
        if (menuText === '커뮤니티') {
          window.location.href = '/alldayrun/main.do';
        } else if (menuText === '코스') {
          window.location.href = '/alldayrun/course/coursemain.do';
        } //else if문 더 추가해야 함

      // 모바일 화면일 때: 기존 토글 방식
      } else {
        event.preventDefault(); // 페이지 이동을 막음
        menuBtn.classList.toggle('active'); // CSS 애니메이션을 위한 클래스 토글
      }
    });
  });
});

/*document.addEventListener('DOMContentLoaded', () => {

  // 화면이 데스크탑 크기인지 확인하는 함수 (801px 이상)
  const isDesktop = () => window.matchMedia('(min-width: 801px)').matches;

  // 사이드바의 모든 메인 메뉴 버튼에 대해 설정
  document.querySelectorAll('.menu-btn').forEach(menuBtn => {
    
    // 각 버튼에 필요한 요소들을 미리 찾아둠
    const menuItem = menuBtn.parentElement;       // 버튼을 감싸는 <li>
    const submenu = menuBtn.nextElementSibling;  // 하위 메뉴 <ul>

    // 하위 메뉴가 없는 경우를 대비한 안전장치
    if (!submenu || !submenu.classList.contains('submenu')) {
      return;
    }

    // 1. 데스크탑용: 마우스를 올렸을 때 (Hover)
    menuItem.addEventListener('mouseover', () => {
      // 데스크탑 화면일 때만 실행
      if (isDesktop()) {
        menuBtn.classList.add('active'); // 화살표 아이콘 등 활성화 스타일 적용
        submenu.style.display = 'block'; // 하위 메뉴 펼치기
      }
    });

    // 2. 데스크탑용: 마우스가 벗어났을 때
    menuItem.addEventListener('mouseout', () => {
      // 데스크탑 화면일 때만 실행
      if (isDesktop()) {
        menuBtn.classList.remove('active'); // 활성화 스타일 제거
        submenu.style.display = 'none';      // 하위 메뉴 숨기기
      }
    });

    // 3. 클릭 이벤트 (데스크탑과 모바일에서 다르게 동작)
    menuBtn.addEventListener('click', (event) => {
      
      // 데스크탑 화면일 때: 페이지 이동
      if (isDesktop()) {
        const menuText = menuBtn.textContent.trim();
        
        // 클릭한 버튼의 텍스트에 따라 다른 페이지로 이동
        if (menuText === '커뮤니티') {
          // TODO: 실제 커뮤니티 메인 페이지 URL로 변경하세요.
          window.location.href = '${pageContext.request.contextPath}/community/main.do';
        } else if (menuText === '코스') {
          // TODO: 실제 코스 메인 페이지 URL로 변경하세요.
          window.location.href = '${pageContext.request.contextPath}/course/coursemain.do';
        }

      // 모바일 화면일 때: 기존 토글 방식
      } else {
        event.preventDefault(); // 페이지 이동을 막음
        menuBtn.classList.toggle('active');
        submenu.style.display = menuBtn.classList.contains('active') ? 'block' : 'none';
      }
    });
  });
});*/

/*document.addEventListener("DOMContentLoaded", () => {
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
});*/


/*document.addEventListener("DOMContentLoaded", () => {

	// 기존 아코디언 메뉴 기능
	document.querySelectorAll(".menu-btn").forEach(btn => {
		btn.addEventListener("click", (e) => {
			e.currentTarget.classList.toggle("active");
		});
	});


	// ▼▼▼ 햄버거 메뉴 기능 추가 ▼▼▼
	const hamburgerBtn = document.querySelector('.hamburger-btn');
	const sidebar = document.querySelector('#sidebar');

	if (hamburgerBtn && sidebar) {
		hamburgerBtn.addEventListener('click', () => {
			sidebar.classList.toggle('is-open');
			hamburgerBtn.classList.toggle('is-open');
		});
	}
	// ▲▲▲ 햄버거 메뉴 기능 추가 ▲▲▲
});*/

/*document.addEventListener("DOMContentLoaded", () => {

	// 기존 아코디언 메뉴 기능
	document.querySelectorAll(".menu-btn").forEach(btn => {
		btn.addEventListener("click", (e) => {
			e.currentTarget.classList.toggle("active");
		});
	});


	// 햄버거 메뉴 기능
	const hamburgerBtn = document.querySelector('.hamburger-btn');
	const sidebar = document.querySelector('#sidebar');

	if (hamburgerBtn && sidebar) {
		hamburgerBtn.addEventListener('click', () => {
			sidebar.classList.toggle('is-open');
			hamburgerBtn.classList.toggle('is-open');
		});
	}
	// ▲▲▲ 햄버거 메뉴 기능 추가 ▲▲▲
});*/