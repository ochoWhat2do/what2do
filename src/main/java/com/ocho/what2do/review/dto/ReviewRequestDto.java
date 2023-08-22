package com.ocho.what2do.review.dto;

import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String content;

    public Review toEntity(String title, Long order, User user) {
        return Review.builder()
                .title(title)
                .content(content)
                .order(order)
                .user(user)
                .build();
    }
}
