package com.ocho.what2do.review.dto;

import com.ocho.what2do.common.file.S3FileDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewRequestDto {
    //    @NotBlank(message = "리뷰 제목은 필수입니다.")
    private String title;

    //    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String content;

    private List<S3FileDto> attachment;

    private Long orderNo;


    public List<S3FileDto> getAttachment() {
        return attachment;
    }
}
