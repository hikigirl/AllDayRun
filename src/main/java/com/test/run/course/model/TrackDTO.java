package com.test.run.course.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TrackDTO {
	//tblTrack
	private String trackSeq;
	private String startSpotSeq;
	private String endSpotSeq;
	private String courseLength;
	private String courseSeq;
	
}
