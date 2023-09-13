package com.ocho.what2do.review.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ReviewListResponseDto {
  private int totalCount;   // 총 리뷰 건수
  private int pageCount;    // 총 리뷰 건수 / 10

  private List<ReviewResponseDto> reviewList;

  public ReviewListResponseDto(int totalCount, List<ReviewResponseDto> reviewList) {
    this.totalCount = totalCount;
    this.pageCount = totalCount / 10 + 1;
    this.reviewList = reviewList;
  }
}
