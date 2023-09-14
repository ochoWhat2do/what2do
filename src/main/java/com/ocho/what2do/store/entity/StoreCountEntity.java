package com.ocho.what2do.store.entity;

import lombok.Builder;
import lombok.Getter;

// 조회용도로만사용하는 엔티티(리뷰 수 별 가게 리스트 조회)
@Getter
@Builder
public class StoreCountEntity {
  Long storeId;
  Long reviewCount;

  public StoreCountEntity(Long storeId, Long reviewCount) {
    this.storeId = storeId;
    this.reviewCount = reviewCount;
  }

}
