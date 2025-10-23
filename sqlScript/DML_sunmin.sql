--tblCourse
INSERT INTO tblCourse(courseSeq, courseName, courseApproval, accountId) VALUES (seqCourse.nextVal, '산책 코스', default, 'admin@naver.com');

--tblSpot
INSERT INTO tblSpot(spotSeq, place, lat, lng, courseSeq, spotStep) VALUES (seqSpot.nextVal, '이촌역', 37.52358457743854, 126.97520295392947, 21, 0);

--tblTrack
INSERT INTO tblTrack(trackSeq, courseSeq, startspotSeq, endspotSeq, courselength) VALUES (seqTrack.nextVal, 21, 22, 0, 21);