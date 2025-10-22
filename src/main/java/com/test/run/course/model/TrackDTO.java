package com.test.run.course.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 코스 경로(tblTrack)의 정보를 담은 DTO
 * 코스 등록 기능에서 사용
 */
@Getter
@Setter
@ToString
public class TrackDTO {
	
	private String trackSeq;
	private String startSpotSeq;
	private String endSpotSeq;
	private String courseLength;
	private String courseSeq;

}
