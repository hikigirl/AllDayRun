# JSP/Servlet 프로젝트 - 올데이런
### 새 프로젝트 만들기
- New dynamic web project
  - project name: AllDayRun
  - context root: alldayrun
  - web.xml 체크


### 파일, 라이브러리 세팅
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
