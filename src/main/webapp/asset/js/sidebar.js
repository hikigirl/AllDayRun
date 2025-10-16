document.addEventListener('DOMContentLoaded', () => {

	document.querySelectorAll('.menu-btn').forEach(menuBtn => {

		// 버튼과 그 부모(li)를 변수에 저장
		const menuItem = menuBtn.parentElement;

		// 1. 마우스를 올렸을 때 하위 메뉴 펼치기
		menuItem.addEventListener('mouseover', () => {
			menuBtn.classList.add('active');
		});

		// 2. 마우스가 벗어났을 때 하위 메뉴 숨기기
		menuItem.addEventListener('mouseout', () => {
			menuBtn.classList.remove('active');
		});

		// 3. 클릭했을 때 페이지 이동
		// menuBtn.addEventListener('click', () => {
		// 	const menuText = menuBtn.textContent.trim();

		// 	if (menuText === '커뮤니티') {
		// 		window.location.href = '/alldayrun/main.do';
		// 	} else if (menuText === '코스') {
		// 		window.location.href = '/alldayrun/course/coursemain.do';
		// 	} 
		// 	// TODO: else if 문으로 다른 메뉴들의 페이지 이동 로직을 여기에 추가
		// });
	});

});