package com.ocho.what2do.comment.dto;

import com.ocho.what2do.comment.entity.CommentLike;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentLikeResponseDto {
    private Long commentId;
    private Long userId;
    private boolean liked;
    private String nickname;
    private String email;
    private String title;

    public CommentLikeResponseDto(CommentLike commentLike) {
        this.email = commentLike.getUser().getEmail();
        this.nickname = commentLike.getUser().getNickname();
        this.title = commentLike.getComment().getReview().getTitle();

    }

}
