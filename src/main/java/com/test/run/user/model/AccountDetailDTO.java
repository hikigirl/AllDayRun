package com.test.run.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 개인상세정보(tblAccountInfoDetail) 관련 DTO
 * 회원가입 기능에서 사용한다.
 */
@Getter
@Setter
@ToString
public class AccountDetailDTO {
	private String accountInfoDetailSeq;
	private String accountId;
	private String name;
	private String phoneNum;
	private String birthday;
	private String gender;
	private String regionCity;
	private String regionCounty;
	private String regionDistrict;
	private String exerciseFrequency;
	private String joinDate;
	
}
