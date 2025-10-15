<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<aside id="sidebar">
	<nav>
		<%-- ❗전체를 <ul> 태그로 한번 감싸주세요 --%>
		<ul>
			
			<%-- ▼▼▼ 모바일용 로그인/회원가입 링크 추가 ▼▼▼ --%>
			

			<li class="sidebar-buttons">
				<a href="#">로그인</a>
				<a href="#">회원가입</a>
			</li>
			
			<hr class="mobile-only-hr">
			<%-- ▲▲▲ 모바일용 로그인/회원가입 링크 추가 ▲▲▲ --%>


			<li><button class="menu-btn">커뮤니티</button>
				<ul class="submenu">
					<li><a href="#">자유게시판</a></li>
					<li><a href="#">공지 및 건의게시판</a></li>
					<li><a href="#">좋아요한 글</a></li>
					<li><a href="#">스크랩한 글</a></li>
				</ul>
			</li>

			<hr>

			<li><button class="menu-btn">코스</button>
				<ul class="submenu">
					<li><a href="#">코스 목록</a></li>
					<li><a href="/alldayrun/course/courseregister.do">코스 등록 신청</a></li>
				</ul>
			</li>
			
			<hr>
			
			<li><button class="menu-btn">크루</button>
				<ul class="submenu">
					<li><a href="#">크루전용 게시판</a></li>
					<li><a href="#">크루전용 게시판</a></li>
					<li><a href="#">크루전용 게시판</a></li>
					<li><a href="#">크루전용 게시판</a></li>
				</ul>
			</li>

		</ul>
	</nav>
</aside>