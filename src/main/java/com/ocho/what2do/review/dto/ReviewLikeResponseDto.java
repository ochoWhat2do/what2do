package com.ocho.what2do.review.dto;

import com.ocho.what2do.review.entity.ReviewLike;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewLikeResponseDto {
    private Long reviewId;
    private Long userId;
    private boolean liked;
    private String nickname;
    private String email;
    private String title;

    public ReviewLikeResponseDto(ReviewLike reviewLike) {
        this.email = reviewLike.getUser().getEmail();
        this.nickname = reviewLike.getUser().getNickname();
        this.title = reviewLike.getReview().getTitle();
        this.liked = reviewLike.getReview().getLikes().stream().filter(v -> v.getUser().equals(reviewLike.getUser())).toList().size() > 0;
    }

}
