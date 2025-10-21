package com.test.run.course.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseCardDTO {
	// 검색 결과를 표시하기 위한 하나의 카드에 불러올 정보를 위한 dto
	private String courseSeq;
	private String courseName;
	private double totalDistance;
	private int favoriteCount;
	private String curator;

	private String accountId;
}
