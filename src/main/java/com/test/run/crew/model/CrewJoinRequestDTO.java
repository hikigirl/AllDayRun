package com.test.run.crew.model;

import lombok.Data;

@Data
public class CrewJoinRequestDTO {
    private String crewJoinSeq;
    private String crewSeq;
    private String accountId;
    private String requestState;
    private String nickname; // To display the requester's nickname
}
