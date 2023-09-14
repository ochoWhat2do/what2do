package com.ocho.what2do.review.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ReviewListResponseDto {
  private Long totalCount;   // 총 리뷰 건수
  private int pageCount;    // 페이지 개수

  private List<ReviewResponseDto> reviewList;

  public ReviewListResponseDto(Long totalCount, int pageCount, List<ReviewResponseDto> reviewList) {
    this.totalCount = totalCount;
    this.pageCount = pageCount;
    this.reviewList = reviewList;
  }
}
