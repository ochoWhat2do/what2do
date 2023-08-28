package com.ocho.what2do.review.dto;

import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
//    @NotBlank(message = "리뷰 제목은 필수입니다.")
    private String title;

//    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String content;


    public Review toEntity(String title, Long orderNo, User user) {
        return Review.builder()
                .title(title)
                .content(content)
                .orderNo(orderNo)
                .user(user)
                .build();
    }
}
