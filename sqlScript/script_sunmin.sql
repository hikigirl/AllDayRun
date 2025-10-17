--최종 ERD(251014, 집에서 수정함)
--1. 계정 기본 정보
CREATE SEQUENCE seqAccountInfo;
DROP TABLE tblAccountInfo;
CREATE TABLE tblAccountInfo (
	accountId VARCHAR2(100) NOT NULL,
	password VARCHAR2(30) DEFAULT '1q2w3e4r' NOT NULL,
	profilePhoto VARCHAR2(150) DEFAULT 'pic.png' NOT NULL,
	nickname VARCHAR2(50) NOT NULL,
	accountRole VARCHAR2(50) DEFAULT '일반' NOT NULL,
	accountSeq NUMBER NOT NULL, 
	accountLevel NUMBER DEFAULT 1 NOT NULL,
	registerType VARCHAR2(50) DEFAULT '기본' NOT NULL,
	accountCategory VARCHAR2(50) DEFAULT '활성' NOT NULL
);
ALTER TABLE tblAccountInfo ADD CONSTRAINT "PK_TBLACCOUNTINFO" PRIMARY KEY (accountId);

INSERT INTO tblAccountInfo(accountId, password, profilePhoto, nickname, accountRole, accountSeq, accountLevel, registerType, accountCategory) VALUES (
	'admin@naver.com', DEFAULT, DEFAULT, '관리자', '관리자', seqAccountInfo.nextVal, DEFAULT, DEFAULT, DEFAULT
);

SELECT * FROM tblAccountInfo;

COMMIT;

--2. tblCourse
CREATE SEQUENCE seqCourse;
CREATE TABLE tblCourse (
	courseSeq		NUMBER			NOT NULL,
	courseName		VARCHAR2(50)	NOT NULL,
	courseApproval	VARCHAR2(50)	DEFAULT '대기'	NOT NULL,
	accountId		VARCHAR2(100)	NOT NULL
);

ALTER TABLE tblCourse ADD CONSTRAINT "PK_TBLCOURSE" PRIMARY KEY (courseSeq);

ALTER TABLE tblCourse ADD CONSTRAINT "FK_tblAccountInfo_TO_tblCourse_1" FOREIGN KEY (accountId) REFERENCES tblAccountInfo (accountId);

--3. tblSpot
CREATE SEQUENCE seqSpot;
CREATE TABLE tblSpot (
	spotSeq		NUMBER			NOT NULL,
	place		VARCHAR2(50)	NOT NULL,
	lat			NUMBER			NOT NULL,
	lng			NUMBER			NOT NULL,
	courseSeq	NUMBER			NOT NULL,
	spotStep	NUMBER			NOT NULL
);

ALTER TABLE tblSpot ADD CONSTRAINT "PK_TBLSPOT" PRIMARY KEY (spotSeq);

ALTER TABLE tblSpot ADD CONSTRAINT "FK_tblCourse_TO_tblSpot_1" FOREIGN KEY (courseSeq) REFERENCES tblCourse (courseSeq);

--4. tblTrack
CREATE SEQUENCE seqTrack;
CREATE TABLE tblTrack (
	trackSeq		NUMBER		NOT NULL,
	startspotSeq	NUMBER		NOT NULL,
	endspotSeq		NUMBER		NOT NULL,
	courselength	NUMBER		NOT NULL,
	courseSeq		NUMBER		NOT NULL
);

ALTER TABLE tblTrack ADD CONSTRAINT "PK_TBLTRACK" PRIMARY KEY (trackSeq);

ALTER TABLE tblTrack ADD CONSTRAINT "FK_tblSpot_TO_tblTrack_1" FOREIGN KEY (startspotSeq) REFERENCES tblSpot (spotSeq);

ALTER TABLE tblTrack ADD CONSTRAINT "FK_tblSpot_TO_tblTrack_2" FOREIGN KEY (endspotSeq) REFERENCES tblSpot (spotSeq);

ALTER TABLE tblTrack ADD CONSTRAINT "FK_tblCourse_TO_tblTrack_1" FOREIGN KEY (courseSeq) REFERENCES tblCourse (courseSeq);


UPDATE TBLCOURSE SET courseapproval = '승인' WHERE COURSESEQ>40;
SELECT * FROM tblCourse;



--5. tblScrapCourse
CREATE SEQUENCE seqScrapCourse;
CREATE TABLE tblScrapCourse (
	scrapCourseSeq	NUMBER			NOT NULL,
	accountId		VARCHAR2(100)	NOT NULL,
	courseSeq		NUMBER			NOT NULL
);

ALTER TABLE tblScrapCourse ADD CONSTRAINT "PK_TBLSCRAPCOURSE" PRIMARY KEY (scrapCourseSeq);

ALTER TABLE tblScrapCourse ADD CONSTRAINT "FK_tblAccountInfo_TO_tblScrapCourse_1" FOREIGN KEY (accountId) REFERENCES tblAccountInfo (accountId);

ALTER TABLE tblScrapCourse ADD CONSTRAINT "FK_tblCourse_TO_tblScrapCourse_1" FOREIGN KEY (courseSeq) REFERENCES tblCourse (courseSeq);

SELECT * FROM tblscrapcourse;

--6. 코스 댓글 테이블(tblCommentCourse)
CREATE SEQUENCE seqCommentCourse;
CREATE TABLE tblCommentCourse (
	courseCommentSeq	NUMBER				NOT NULL,
	courseSeq			NUMBER				NOT NULL,
	accountId			VARCHAR2(100)		NOT NULL,
	content				VARCHAR2(1500)		NOT NULL,
	regdate				DATE	DEFAULT sysdate 	NOT NULL
);

ALTER TABLE tblCommentCourse ADD CONSTRAINT "PK_TBLCOMMENTCOURSE" PRIMARY KEY (courseCommentSeq);

ALTER TABLE tblCommentCourse ADD CONSTRAINT "FK_tblCourse_TO_tblCommentCourse_1" FOREIGN KEY (courseSeq) REFERENCES tblCourse (courseSeq);

ALTER TABLE tblCommentCourse ADD CONSTRAINT "FK_tblAccountInfo_TO_tblCommentCourse_1" FOREIGN KEY (accountId) REFERENCES tblAccountInfo (accountId);

SELECT * FROM tblcommentcourse;

--7. 검색 결과 카드에 표시하기 위한 뷰 생성(vwCourseCards)
CREATE OR REPLACE VIEW vwCourseCards AS
SELECT
    c.courseSeq,
    c.courseName,
    NVL(SUM(t.courselength), 0) as totalDistance,
    (SELECT COUNT(*) FROM tblScrapCourse s WHERE s.courseSeq = c.courseSeq) as FAVORITECOUNT,
    ai.NICKNAME as curator,
    c.accountId
FROM tblCourse c
LEFT JOIN tblTrack t ON c.courseSeq = t.courseSeq
JOIN tblAccountInfo ai ON c.accountId = ai.accountId
WHERE c.courseApproval = '승인'
GROUP BY c.courseSeq, c.courseName, ai.NICKNAME, c.accountId;

SELECT * FROM vwCourseCards;

SELECT * FROM vwCourseCards WHERE courseName LIKE '%한강%' ORDER BY favoriteCount DESC, courseSeq DESC;

SELECT * FROM vwCourseCards ORDER BY FAVORITECOUNT DESC, courseSeq DESC;

--courseDAO의 getPopularCourses() 메서드에서 사용하는 쿼리. 뷰에서 상위 6개 요소만 가져온다.
SELECT * FROM (SELECT * FROM vwCourseCards ORDER BY FAVORITECOUNT DESC, courseSeq DESC) WHERE rownum <=6;

/*SELECT
    c.courseSeq, c.courseName,                               -- (A) 기본 코스 정보
    NVL(SUM(t.courselength), 0) as totalDistance,         -- (B) 코스 총 거리 계산
    (SELECT COUNT(*) FROM tblFavorite f
     WHERE f.courseSeq = c.courseSeq) as favoriteCount, -- (C) 즐겨찾기 수 계산
    ai.accountNickname as curator                       -- (D) 등록자 닉네임
FROM tblCourse c                                             -- (E) 기준 테이블: 코스
LEFT JOIN tblTrack t ON c.courseSeq = t.courseSeq          -- (F) 코스와 경로 조각 연결
JOIN tblAccountInfo ai ON c.accountId = ai.accountId     -- (G) 코스와 사용자 정보 연결
WHERE c.courseApproval = '승인' AND c.courseName LIKE '%코스%'  -- (H) 검색 조건
GROUP BY c.courseSeq, c.courseName, ai.accountNickname     -- (I) 요약 기준
ORDER BY favoriteCount DESC, c.courseSeq DESC;              -- (J) 정렬 순서*/

/*SELECT
    c.courseSeq, c.courseName,
    NVL(SUM(t.courselength), 0) as totalDistance,
    (SELECT COUNT(*) FROM tblScrapCourse f WHERE f.courseSeq = c.courseSeq) as favoriteCount,
    ai.NICKNAME as curator
FROM tblCourse c
LEFT JOIN tblTrack t ON c.courseSeq = t.courseSeq
JOIN tblAccountInfo ai ON c.accountId = ai.accountId
WHERE c.courseApproval = '승인' AND c.courseName LIKE '%코스%' -- 이 부분을 수정
GROUP BY c.courseSeq, c.courseName, ai.NICKNAME
ORDER BY favoriteCount DESC, c.courseSeq DESC;*/

--
SELECT * FROM TBLACCOUNTINFO;
SELECT * FROM tblaccountInfoDetail;
SELECT * FROM vwCourseCards;
SELECT regionCity, regionCounty, regionDistrict FROM tblAccountInfoDetail WHERE accountId = 'test@naver.com';

SELECT * FROM (
	SELECT v.* FROM vwCourseCards v 
	INNER JOIN tblSpot s ON v.courseSeq = s.courseSeq  
	WHERE s.place LIKE ?
	GROUP BY v.courseSeq, v.courseName, v.totalDistance, v.courseScrapCount, v.curator, v.accountId 
	ORDER BY v.courseScrapCount DESC
) WHERE ROWNUM <= ?

SELECT * FROM (SELECT v.* FROM vwCourseCards v INNER JOIN tblSpot s ON v.courseSeq = s.courseSeq WHERE s.place LIKE ? GROUP BY v.courseSeq, v.courseName, v.totalDistance, v.favoriteCount, v.curator, v.accountId ORDER BY v.favoriteCount DESC) WHERE ROWNUM <= ?;

SELECT * FROM TBLACCOUNTINFO;

DELETE FROM TBLACCOUNTINFO WHERE accountid='wleer157@gmail.com';


SELECT * FROM tblaccountinfo;
SELECT * FROM tblspot;

SELECT v.* FROM vwCourseCards v INNER JOIN tblSpot s ON v.courseSeq = s.courseSeq WHERE s.place LIKE '%용산구%' GROUP BY v.courseSeq, v.courseName, v.totalDistance, v.favoriteCount, v.curator, v.accountId ORDER BY v.favoriteCount DESC;

SELECT * FROM (
	SELECT v.* FROM vwCourseCards v INNER JOIN tblSpot s ON v.courseSeq = s.courseSeq WHERE s.place LIKE '%용산%' GROUP BY v.courseSeq, v.courseName, v.totalDistance, v.favoriteCount, v.curator, v.accountId ORDER BY v.favoriteCount DESC
) WHERE ROWNUM <= ?

SELECT * FROM (SELECT v.* FROM vwCourseCards v INNER JOIN tblSpot s ON v.courseSeq = s.courseSeq WHERE (s.place LIKE ? OR s.place LIKE ?) GROUP BY v.courseSeq, v.courseName, v.totalDistance, v.courseScrapCount, v.curator, v.accountIdORDER BY v.courseScrapCount DESC) WHERE ROWNUM <= ?


