package com.ocho.what2do.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequestDto {
    private String content;
    private Long review_Id;
}
