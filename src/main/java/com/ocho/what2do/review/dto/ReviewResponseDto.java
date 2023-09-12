package com.ocho.what2do.review.dto;

import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.review.entity.ReviewLike;
import com.ocho.what2do.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class ReviewResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCount;
    private List<S3FileDto> attachment;
    private Long orderNo;
    private int rate;
    private boolean liked;
    private String createEmail;
    private Long storeId;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
        this.likeCount = review.getLikes().size();
        this.attachment = review.getAttachment();
        this.orderNo = review.getOrderNo();
        this.rate = review.getRate();
        this.createEmail = review.getUser().getEmail();
        this.storeId = review.getStore().getId();
    }

    public ReviewResponseDto(Review review, User loginUser) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
        this.likeCount = review.getLikes().size();
        this.attachment = review.getAttachment();
        this.orderNo = review.getOrderNo();
        this.rate = review.getRate();
        this.createEmail = review.getUser().getEmail();
        Optional<ReviewLike> userLike = review.getLikes().stream().filter(v -> v.getUser().getId().equals(loginUser.getId())).findFirst();
        if (userLike.isPresent()) {
            this.liked = true;
        } else {
            this.liked = false;
        }
        this.storeId = review.getStore().getId();
    }
}
