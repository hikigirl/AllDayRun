package com.test.run.course.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 코스 표시하는 하나의 카드에 불러올 정보를 위한 DTO(vwCourseCard)
 * 코스 메인페이지, 코스 검색 결과 표시에 사용
 */
@Getter
@Setter
@ToString
public class CourseCardDTO {

	private String courseSeq;
	private String courseName;
	private double totalDistance;
	private int favoriteCount;
	private String curator;

	private String accountId;
}
