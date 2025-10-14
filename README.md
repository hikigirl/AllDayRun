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


</div>
</details>

<br>
<details>
<summary>트러블슈팅</summary>
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
  - 

__2025.10.14__
- erd 관련
  - 코스 지점(tblSpot), 코스 경로(tblTrack), 코스(tblCourse)
  - 코스 지점(좌표 하나)가 있고 코스 조각들(좌표 to 좌표)가 있고 조각들이 모인 코스가 있음
  - 기존에는 코스가 코스조각을 참조하는 형태였는데 선후관계를 변경하였음.
  - 코스 조각이 코스를 참조하는 형태로 변경

</div>
</details>