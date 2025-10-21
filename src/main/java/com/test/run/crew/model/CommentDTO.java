package com.test.run.crew.model;

import lombok.Data;

@Data
public class CommentDTO {

	private int commentSeq ;
	private int crewSeq;
	private int boardContentSeq;
	private String accountId;
	private String content;
	private String regdate;
	private String nickname;
	
	
}
