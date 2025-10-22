package com.test.run.crew.model;

import lombok.Data;

/**
 * 크루 가입 관련 DTO
 * 크루 가입 기능에서 사용한다.
 */
@Data
public class CrewJoinRequestDTO {
    private String crewJoinSeq;
    private String crewSeq;
    private String accountId;
    private String requestState;
    private String nickname; // To display the requester's nickname
}

