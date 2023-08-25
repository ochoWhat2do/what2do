package com.ocho.what2do.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewLikeResponseDto {
    private Long reviewId;
    private Long userId;
    private boolean liked;

    public ReviewLikeResponseDto(Long reviewId, Long userId, boolean liked) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.liked = liked;
    }

}
