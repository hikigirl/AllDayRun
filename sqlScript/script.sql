--계정정보 테이블
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

--tblSpot
CREATE SEQUENCE seqSpot;

CREATE TABLE tblSpot(
	spotSeq NUMBER NOT NULL,
	place varchar2(50) NOT NULL, --이름을 저장
	lat number NOT NULL,
	lng number NOT NULL
);
ALTER TABLE tblSpot ADD CONSTRAINT "PK_TBLSPOT" PRIMARY KEY (spotSeq);



INSERT INTO tblSpot(spotSeq, place, lat, lng) VALUES (seqSpot.nextVal, ?, ?, ?);
DELETE FROM tblspot WHERE (spotseq=4);

SELECT * FROM tblSpot;

--tblTrack
CREATE SEQUENCE seqTrack;
CREATE TABLE tblTrack (
	trackSeq NUMBER NOT NULL,
	startspotSeq NUMBER NOT NULL,
	endspotSeq NUMBER NOT NULL,
	courselength NUMBER NOT NULL
);
ALTER TABLE tblTrack ADD CONSTRAINT "PK_TBLTRACK" PRIMARY KEY (trackSeq);

ALTER TABLE tblTrack ADD CONSTRAINT "FK_tblSpot_TO_tblTrack_1" FOREIGN KEY (startspotSeq) REFERENCES tblSpot (spotSeq);

ALTER TABLE tblTrack ADD CONSTRAINT "FK_tblSpot_TO_tblTrack_2" FOREIGN KEY (endspotSeq) REFERENCES tblSpot (spotSeq);

SELECT * FROM tblTrack;


--tblCourse
CREATE SEQUENCE seqCourse;
CREATE TABLE tblCourse (
	courseSeq NUMBER NOT NULL,
--	trackSeq NUMBER NOT NULL,
	courseName varchar2(50) NOT NULL,
	courseApproval VARCHAR2(50) DEFAULT '대기' NOT NULL,
	accountId varchar2(100) NOT NULL
);

ALTER TABLE tblCourse ADD CONSTRAINT "PK_TBLCOURSE" PRIMARY KEY (courseSeq);

ALTER TABLE tblCourse ADD CONSTRAINT "FK_tblTrack_TO_tblCourse_1" FOREIGN KEY (trackSeq) REFERENCES tblTrack (trackSeq);

ALTER TABLE tblCourse ADD CONSTRAINT "FK_tblAccountInfo_TO_tblCourse_1" FOREIGN KEY (accountId) REFERENCES tblAccountInfo (accountId);

SELECT * FROM tblCourse;

--스키마 수정(tblCourse)
-- 1. 기존 테이블에서 trackSeq 컬럼 제거
ALTER TABLE tblCourse DROP COLUMN trackSeq;

-- 2. tblTrack에 courseSeq 컬럼 추가 및 외래 키 설정
ALTER TABLE tblTrack ADD courseSeq NUMBER NOT NULL;
ALTER TABLE tblTrack ADD CONSTRAINT fk_track_course FOREIGN KEY (courseSeq) REFERENCES tblCourse(courseSeq);





--집가서...(251014)
DROP TABLE TBLTRACK;
DROP TABLE tblspot;
DROP TABLE tblCourse;

--1. tblSpot
CREATE TABLE tblSpot (
	spotSeq	NUMBER				NOT NULL,
	place	VARCHAR2(300)		NOT NULL,
	lat		number				NOT NULL,
	lng		number				NOT NULL
);
ALTER TABLE tblSpot ADD CONSTRAINT "PK_TBLSPOT" PRIMARY KEY (spotSeq);

SELECT * FROM tblSpot;

--2. tblCourse
CREATE TABLE tblCourse (
	courseSeq		NUMBER				NOT NULL,
	courseName		VARCHAR2(50)		NOT NULL,
	courseApproval	VARCHAR2(50)		DEFAULT '대기'	NOT NULL,
	accountId		VARCHAR2(100)		NOT NULL
);

ALTER TABLE tblCourse ADD CONSTRAINT "PK_TBLCOURSE" PRIMARY KEY (courseSeq);
ALTER TABLE tblCourse ADD CONSTRAINT "FK_tblAccountInfo_TO_tblCourse_1" FOREIGN KEY (accountId)
REFERENCES tblAccountInfo (accountId);

SELECT * FROM tblCourse;

--3. tblTrack
CREATE TABLE tblTrack (
	trackSeq		NUMBER		NOT NULL,
	startspotSeq	NUMBER		NOT NULL,
	endspotSeq		NUMBER		NOT NULL,
	courselength	NUMBER		NOT NULL,
	courseSeq		NUMBER		NOT NULL
);

ALTER TABLE tblTrack ADD CONSTRAINT "PK_TBLTRACK" PRIMARY KEY (trackSeq);

ALTER TABLE tblTrack ADD CONSTRAINT "FK_tblSpot_TO_tblTrack_1" FOREIGN KEY (startspotSeq)
REFERENCES tblSpot (spotSeq);

ALTER TABLE tblTrack ADD CONSTRAINT "FK_tblSpot_TO_tblTrack_2" FOREIGN KEY (endspotSeq) REFERENCES tblSpot (spotSeq);

ALTER TABLE tblTrack ADD CONSTRAINT "FK_tblCourse_TO_tblTrack_1" FOREIGN KEY (courseSeq) REFERENCES tblCourse (courseSeq);

SELECT * FROM tblTrack;



--테스트(DML)
SELECT accountId FROM tblAccountinfo;
INSERT INTO tblSpot(spotSeq, place, lat, lng) VALUES (seqSpot.nextVal, '지점명', 34, 12);
INSERT INTO tblSpot(spotSeq, place, lat, lng) VALUES (seqSpot.nextVal, '지점명', 24, 20);

SELECT * FROM tblspot;

INSERT INTO tblcourse(courseSeq, COURSENAME, courseapproval, ACCOUNTID) VALUES (seqCourse.nextval, '코스이름', DEFAULT, 'admin@naver.com');

SELECT * FROM tblcourse;

INSERT INTO tbltrack VALUES (seqTrack.nextVal, 7, 8, 2.2, 1);
INSERT INTO tbltrack VALUES (seqTrack.nextVal, 7, 8, 2.2, 2);
SELECT * FROM tbltrack;

