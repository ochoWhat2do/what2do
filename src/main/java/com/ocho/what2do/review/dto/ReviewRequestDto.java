package com.ocho.what2do.review.dto;

import com.ocho.what2do.common.file.S3FileDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewRequestDto {
    @NotBlank(message = "리뷰 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String content;

    private List<S3FileDto> attachment;

    private Long orderNo = 1L; // 관리용도로 사용할것

    private Long rate; // 평점

    private Long storeId;


    public List<S3FileDto> getAttachment() {
        return attachment;
    }

    @Builder

    public ReviewRequestDto(String title, String content, Long orderNo, Long storeId) {
        this.title = title;
        this.content = content;
        this.orderNo = orderNo;
        this.storeId = storeId;
    }
}
