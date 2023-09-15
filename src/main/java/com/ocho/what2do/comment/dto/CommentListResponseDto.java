package com.ocho.what2do.comment.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class CommentListResponseDto {
    private Long totalCount;   // 총 리뷰 건수
    private int pageCount;    // 페이지 개수

    private List<CommentResponseDto> commentList;

    public CommentListResponseDto(Long totalCount, int pageCount, List<CommentResponseDto> commentList) {
        this.totalCount = totalCount;
        this.pageCount = pageCount;
        this.commentList = commentList;
    }
}
