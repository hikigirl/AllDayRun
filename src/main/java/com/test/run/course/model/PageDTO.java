package com.test.run.course.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {
    private int currentPage; // 현재 페이지
    private int totalCount; // 총 게시물 수
    private int pageSize = 9; // 한 페이지에 보여줄 카드 개수 (예: 9개)
    private int totalPage; // 총 페이지 수
    private int startPage; // 페이지 블럭의 시작 번호
    private int endPage; // 페이지 블럭의 끝 번호
    private int blockSize = 10; // 한 번에 보여줄 페이지 번호 개수 (예: [1]~[10])

    // 생성자에서 모든 페이징 계산 처리
    public PageDTO(int totalCount, int currentPage) {
        this.totalCount = totalCount;
        this.currentPage = currentPage;

        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
        this.startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
        this.endPage = this.startPage + blockSize - 1;

        if (this.endPage > this.totalPage) {
            this.endPage = this.totalPage;
        }
    }
}
