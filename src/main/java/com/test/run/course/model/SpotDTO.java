package com.test.run.course.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SpotDTO {
	//tblSpot
	private String spotSeq;
	private String place;
	private String lat;
	private String lng;
	private String courseSeq;
	private int spotStep;
}

