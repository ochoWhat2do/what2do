package com.ocho.what2do.comment.dto;

import com.ocho.what2do.comment.entity.Comment;
import com.ocho.what2do.comment.entity.CommentLike;
import com.ocho.what2do.user.dto.UserResponseDto;
import com.ocho.what2do.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private UserResponseDto user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCount;
    private boolean liked;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = getCreatedAt();
        this.modifiedAt = getModifiedAt();
        this.likeCount = getLikeCount();
    }

    @Builder
    public CommentResponseDto(Comment comment, User loginUser) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getCommentLikes().size();
        Optional<CommentLike> userLike = comment.getCommentLikes().stream().filter(v -> v.getUser().getId().equals(loginUser.getId())).findFirst();
        if (userLike.isPresent()) {
            this.liked = true;
        } else {
            this.liked = false;
        }
    }
}