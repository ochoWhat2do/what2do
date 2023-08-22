package com.ocho.what2do.review.dto;

import com.ocho.what2do.comment.dto.CommentResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewDetailDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> comments; // 댓글 목록 등

    public ReviewDetailDto(Long id, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, List<CommentResponseDto> comments) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.comments = comments;
    }
}
