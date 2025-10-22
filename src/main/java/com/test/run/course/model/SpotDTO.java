package com.test.run.course.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 코스 지점(tblSpot)의 정보를 담은 DTO
 * 코스 등록 기능에서 사용
 */
@Getter
@Setter
@ToString
public class SpotDTO {
	// tblSpot
	private String spotSeq;
	private String place;
	private String lat;
	private String lng;
	private String courseSeq;
	private int spotStep;
}
