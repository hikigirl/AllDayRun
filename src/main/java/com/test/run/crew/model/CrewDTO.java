package com.test.run.crew.model;

import lombok.Data;

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
