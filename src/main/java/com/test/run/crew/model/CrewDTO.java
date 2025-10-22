package com.test.run.crew.model;

import lombok.Data;

/**
 * 크루(tblCrew) 관련 DTO
 * 크루 생성 기능에서 사용한다.
 */
@Data
public class CrewDTO {
	
	private int crewSeq;
	private String crewName;
	private String description;
	private int memberCount;
	private String regionCity;
	private String regionCounty;
	private String regionDistrict;
    private String accountId;
	private String crewAttach;
	private double latitude;
	private double longitude;
	private String distance;
	private String nickname;


}
