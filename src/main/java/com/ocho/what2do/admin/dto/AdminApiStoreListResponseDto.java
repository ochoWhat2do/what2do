package com.ocho.what2do.admin.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AdminApiStoreListResponseDto {
  private Long totalCount;   // 총 리뷰 건수
  private int pageCount;    // 페이지 개수

  private List<AdminApiStoreResponseDto> storeList;

  public AdminApiStoreListResponseDto(Long totalCount, int pageCount, List<AdminApiStoreResponseDto> storeList) {
    this.totalCount = totalCount;
    this.pageCount = pageCount;
    this.storeList = storeList;
  }
}
