<h1>올데이런 <small>JSP/Servlet 프로젝트</small></h1>
<br>
<details>
<summary>새 프로젝트 만들기</summary>
<div markdown="1">

- New dynamic web project
  - project name: AllDayRun
  - context root: alldayrun
  - web.xml 체크
  
</div>
</details>

<br>

<details>
<summary>파일, 라이브러리 세팅</summary>
<div markdown="1">

#### 라이브러리
- ojdbc.jar
- myutil.jar
- lombok.jar
- json-simple.jar
- jstl.jar
- cos.jar

#### 패키지, 서블릿
- com.test.run(메인 패키지)
  - Index.java
  - Template.java
- com.test.run.course
  - CourseSearch.java (코스 검색 메인페이지)

#### JSP

- WEB-INF/views
  - index.jsp
  - template.jsp
- views/inc
  - asset.jsp (css 링크 등 여기에 작성)
  - header.jsp (상단 메뉴 조각페이지)
- views/course
  - coursesearch.jsp

#### 공통 리소스

- 프로젝트 루트(AllDayRun)/sqlScript 폴더
  - script.sql
- WEB-INF/views/inc(조각페이지)
  - asset.jsp
  - header.jsp
- webapp/asset/css
  - main.css
- webapp/asset/js
  - main.js
- webapp/asset/images
- webapp/asset/pic
  - 프로필 사진...
- webapp/asset/place
  - 첨부파일 저장용도
- webapp/asset/favicon
  - favicon 저장


</div>
</details>

<br>
<details>
<summary>트러블슈팅&일일 기록</summary>
<div markdown="1">


__2025.09.25__
- 기능 관련
   - 요구분석서 작성을 완료했는데 사용자로부터 받은 데이터를 단순 출력만 할 뿐 활용하는 기능이 부족하다는 사실을 알게 되었다.
   - 사용자가 운동 기록에 입력한 데이터들을 활용해야 할 것 같다는 의견을 제시했다
   - 사용자의 운동 기록 데이터를 코스 추천, 참여할만한 챌린지 추천, 참여할만한 크루 추천 기능 등에 활용 가능할 것 같다.

__2025.10.02__
- DB 관련
  - DB 프로젝트때처럼 Oracle Cloud DB를 사용하려고 했으나 선생님께서 클라우드 db는 성능 면에서 느릴 수 있다고 말씀해주심
  - 로컬 DB 사용하기로 결정


__2025.10.13__
- DB 관련(CourseDAO.java)
  - 코스 지점, 코스 조각이 있는데 현재 구조로는 코스 조각을 저장할 때 문제가 있을 것 같다는 생각을 하게 됨
  - ERD 수정해서 해결

__2025.10.14__
- erd 관련
  - 코스 지점(tblSpot), 코스 경로(tblTrack), 코스(tblCourse)
  - 코스 지점(좌표 하나)가 있고 코스 조각들(좌표 to 좌표)가 있고 조각들이 모인 코스가 있음
  - 기존에는 코스가 코스조각을 참조하는 형태였는데 선후관계를 변경하였음.
  - 코스 조각이 코스를 참조하는 형태로 변경
- javascript 처리 관련 문제
  - 지도에 마커를 찍는 과정에서 사용자가 마지막으로 수정한 사항이 계속 반영되지 않은 채로 서버로 넘어가는 현상이 발생
  - 별명 입력 상자가 언제 활성화되고 비활성화되는지 내부 로직에 의해 꼬여서 발생하게 된 문제(이벤트 우선순위 등등)
  - ~~원래 의도는 해당 상자에 포커스가 가 있으면 마커를 클릭으로 계속 업데이트하는 식으로 진행하려고 했음...(사용자 입장에서 수정하기에 그게 더 편하니까)~~
  - ~~원래 클릭과 드래그 양쪽 다 수정이 가능하게 하려고 했는데 드래그만으로 수정하게끔 구현하기로 결정함~~
  - ~~어려워서 못하겠음...;ㅋㅋ~~
  - 아예 계속 동기화하는 방식으로 바꿈(데이터 날아감 방지를 위해...)

__2025.10.15__
- UI 관련
  - 템플릿 개선
    - main.css
      - header 영역, sidebar 영역, 메인 콘텐츠 영역 등 프로젝트 내 공용 요소의 css
    - sidebar.js
      - 사이드바에 대한 javascript
    - sidebar.jsp
      - 사이드바의 html에 대한 조각 페이지
    - header.jsp
      - 헤더의 html에 대한 조각 페이지
    - asset.jsp
      - head태그 내부에 들어가는 조각 페이지
    - javascriptasset.jsp
      - body 태그 최하단부에 들어가는 스크립트 태그들 연동 용도의 조각 페이지
    - template.jsp
      - 위의 조각페이지들, css, js가 합쳐져 공용으로 복사해서 사용할 jsp 파일
    - 사이드바 CSS 고치는데 엄청 오래걸렸고 모바일 반응형 고치다가 결국 완성은 못함... 이제 그만 고칠거임 일단 데스크탑 화면은 괜찮아보여서 스킵
    - 
- 기능 관련
  - 현재 로그인한 사용자의 정보를 세션에서 받아오는 코드를 추가함
  - 코스 등록 요청 부분에서 현재 직선 경로를 대략적으로 보여주고 있는데, 이 경로의 도보 거리를 넣으려고 했으나 나중에 수정하는 방향으로 결정하였음
  - 직선거리라도 DB에 저장하기로 결정, 수정이 힘드면 일단 update문으로 직접 직선거리를 넣은 뒤 화면상에만 보여주기
  - 이미지 지도 생성하기를 이용, 코스 검색 및 추천에 보여주는 코스 카드에 출력하면 좋을듯
    - https://apis.map.kakao.com/web/sample/staticMapWithMarker/
  - 학원 컴퓨터 로컬 db를 집에서 접속할 수 없다... 네트워크 때문에
    - 더미데이터 넣기 싫어서 수작부리려했는데 실패함,,,
__2025.10.16__
- 사이드바 반응형 웹 부분 css 도저히 해결이 안돼서 그냥 삭제하기로 함
- 사용자 주소 기반 추천 기능
  - 원래 의도한 방향
    - 쿼리가 내가 생각한 방향이랑 다른거같은데 spot에 찍혀있는 좌표를 통해 주소로 파싱 -> 그 주소에서 시, 군, 구를 불러옴 -> 그거랑 사용자 주소랑 비교
    - 계속 네트워크 통신해야돼서 부적합한 방법, 코스 등록할 때 주소를 저장하게끔 만들었어야...
</div>
</details>