package com.test.run.course.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseDTO {
	


	//tblCourse
	private String courseSeq;
	private String courseName;
	private String courseApproval;
	private String accountId; // 사용자 ID (추가 필요)
	
	
}
