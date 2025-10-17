<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<aside id="sidebar">
	<nav>
		<ul>
		<c:choose>
		<%-- ✅ 로그인 상태일 때 --%>
        	<c:when test="${not empty sessionScope.accountId}">
			<li>
				<a href="#" class="menu-btn"><span>러닝 활동 기록</span></a>
				<ul class="submenu">
					<li><a href="#">전체 기록 보기</a></li>
					<li><a href="#">코스별 기록 보기</a></li>
					<li><a href="#">주간/월간 통계</a></li>
					<li><a href="#">친구와 비교하기</a></li>
				</ul>
			</li>

			<hr>

			<li>
				<a href="/alldayrun/course/coursemain.do" class="menu-btn"><span>코스</span></a>
				<ul class="submenu">
					<li><a href="#">코스 목록</a></li>
					<li><a href="/alldayrun/course/courseregister.do">코스 등록 신청</a></li>
				</ul>
			</li>

			<hr>

			<!-- <li>
				<a href="#" class="menu-btn"><span>크루</span></a>
				<ul class="submenu">
					<li><a href="#">크루 서브메뉴 </a></li>
					<li><a href="#">크루 서브메뉴 </a></li>
					<li><a href="#">크루 서브메뉴 </a></li>
					<li><a href="#">크루 서브메뉴 </a></li>
				</ul>
			</li> -->
			<li>
                <a href="/alldayrun/crewmain.do" class="menu-btn"><span>크루</span></a>
                <ul class="submenu">
                    <li><a href="/alldayrun/crewboardlist.do">크루 게시판</a></li>
                    <li><a href="/alldayrun/crewjoinrequestlist.do">크루 관리</a></li>
                </ul>
            </li>
			
			<hr>
			
			<li>
				<a href="#" class="menu-btn"><span>챌린지</span></a>
				<ul class="submenu">
					<li><a href="#">챌린지 서브메뉴</a></li>
					<li><a href="#">챌린지 서브메뉴</a></li>
					<li><a href="#">챌린지 서브메뉴</a></li>
					<li><a href="#">챌린지 서브메뉴</a></li>
				</ul>
			</li>
			
			<hr />
			
			<li>
				<a href="#" class="menu-btn"><span>커뮤니티</span></a>
				<ul class="submenu">
					<li><a href="#">자유게시판</a></li>
					<li><a href="#">공지게시판</a></li>
					<li><a href="/alldayrun/boardsuggestion/list.do">건의게시판</a></li>
					<li><a href="#">좋아요한 글</a></li>
					<li><a href="#">스크랩한 글</a></li>
				</ul>
			</li>
			</c:when>
			<%-- ❌ 로그인 상태가 아닐 때 --%>
        	<c:otherwise>
        	<li>
				<a href="/alldayrun/course/coursemain.do" class="menu-btn"><span>코스</span></a>
				<ul class="submenu">
					<li><a href="/alldayrun/course/courselist.do">코스 목록</a></li>
					<!-- <li><a href="/alldayrun/course/courseregister.do">코스 등록 신청</a></li> -->
				</ul>
			</li>
			<hr>
			
			<li>
				<a href="#" class="menu-btn"><span>커뮤니티</span></a>
				<ul class="submenu">
					<li><a href="#">자유게시판</a></li>
					<li><a href="#">공지게시판</a></li>
					<li><a href="/alldayrun/boardsuggestion/list.do">건의게시판</a></li>
				</ul>
			</li>
        	
        	</c:otherwise>
			
		</c:choose>
		</ul>
	</nav>
</aside>