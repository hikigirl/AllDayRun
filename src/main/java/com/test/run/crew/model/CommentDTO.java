package com.test.run.crew.model;

import lombok.Data;

/**
 * 크루 전용 자유게시판 댓글 (tblCrewCommentGeneral) 관련 DTO
 */
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
