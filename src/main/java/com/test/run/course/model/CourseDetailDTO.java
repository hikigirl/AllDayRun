package com.test.run.course.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseDetailDTO {
    private String courseSeq;
    private String courseName;
    private double totalDistance;
    private int favoriteCount;
    private String curator;
    // 코스 기본 정보
    private String accountId;

    // 상세 정보 (List)
    private List<SpotDTO> spots;
    // private List<ReviewDTO> reviews;

}