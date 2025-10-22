package com.test.run.crew.model;

import lombok.Data;

/**
 * 크루 전용 자유게시판(tblCrewBoardGeneral) 관련 DTO
 */
@Data
public class BoardDTO {
	
		private int boardContentSeq;
		private int crewSeq;
		private String accountId;
		private String title;
		private String content;
		private String attach;
		private String regdate;
		private int readCount;
		private int favoriteCount;
		private int boardContentTypeSeq;
		private String nickname;

		
}
