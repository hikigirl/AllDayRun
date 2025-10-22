package com.test.run.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 개인상세정보(tblAccountInfo) 관련 DTO
 * 회원가입 기능에서 사용한다.
 */
@Getter
@Setter
@ToString
public class AccountDTO {
	private String accountId;
	private String password;
	private String profilePhoto;
	private String nickname;
	private String accountRole;
	private String accountSeq;
	private String accountLevel;
	private String registerType;
	private String accountCategory;
	

}
