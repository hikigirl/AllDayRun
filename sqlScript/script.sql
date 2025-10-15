--최종 ERD(251014, 집에서 수정함)
--1. tblCourse
CREATE TABLE tblCourse (
	courseSeq		NUMBER			NOT NULL,
	courseName		VARCHAR2(50)	NOT NULL,
	courseApproval	VARCHAR2(50)	DEFAULT '대기'	NOT NULL,
	accountId		VARCHAR2(100)	NOT NULL
);

ALTER TABLE tblCourse ADD CONSTRAINT "PK_TBLCOURSE" PRIMARY KEY (courseSeq);

ALTER TABLE tblCourse ADD CONSTRAINT "FK_tblAccountInfo_TO_tblCourse_1" FOREIGN KEY (accountId) REFERENCES tblAccountInfo (accountId);

--2. tblSpot
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

--3. tblTrack
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


UPDATE TBLCOURSE SET courseapproval = '승인' WHERE courseseq=29;
SELECT * FROM tblCourse;

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

--tblScrapCourse
CREATE TABLE tblScrapCourse (
	scrapCourseSeq	NUMBER			NOT NULL,
	accountId		VARCHAR2(100)	NOT NULL,
	courseSeq		NUMBER			NOT NULL
);

ALTER TABLE tblScrapCourse ADD CONSTRAINT "PK_TBLSCRAPCOURSE" PRIMARY KEY (scrapCourseSeq);

ALTER TABLE tblScrapCourse ADD CONSTRAINT "FK_tblAccountInfo_TO_tblScrapCourse_1" FOREIGN KEY (accountId) REFERENCES tblAccountInfo (accountId);

ALTER TABLE tblScrapCourse ADD CONSTRAINT "FK_tblCourse_TO_tblScrapCourse_1" FOREIGN KEY (courseSeq) REFERENCES tblCourse (courseSeq);

SELECT * FROM tblscrapcourse;

--검색 결과 카드에 표시하기 위한 뷰 생성(vwCourseCards)
CREATE OR REPLACE VIEW vwCourseCards AS
SELECT
    c.courseSeq,
    c.courseName,
    NVL(SUM(t.courselength), 0) as totalDistance,
    (SELECT COUNT(*) FROM tblScrapCourse s WHERE s.courseSeq = c.courseSeq) as courseScrapCount,
    ai.NICKNAME as curator,
    c.accountId
FROM tblCourse c
LEFT JOIN tblTrack t ON c.courseSeq = t.courseSeq
JOIN tblAccountInfo ai ON c.accountId = ai.accountId
WHERE c.courseApproval = '승인'
GROUP BY c.courseSeq, c.courseName, ai.NICKNAME, c.accountId;

SELECT * FROM vwCourseCards;

SELECT * FROM vwCourseCards WHERE courseName LIKE '%한강%' ORDER BY favoriteCount DESC, courseSeq DESC;
