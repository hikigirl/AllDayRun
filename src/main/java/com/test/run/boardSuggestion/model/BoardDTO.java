package com.test.run.boardSuggestion.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 건의게시판(tblBoardSuggestion) 관련 DTO
 * 건의게시판 기능에서 사용
 */
@Getter
@Setter
@ToString
public class BoardDTO {

	private String boardContentSeq;
	private String accountId;
	private String title;
	private String content;
	private String attach;
	private String regdate;
	private int readCount;
	private int favoriteCount;
	private int boardContentTypeSeq;

	private String name;

}
