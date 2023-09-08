package com.ocho.what2do.store.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreCategoryListResponseDto {
    private Integer totalCnt;       // 검색 시 api 내의 모든 결과 값 갯수
    private final List<StoreResponseDto> storeCategoryList;

    public StoreCategoryListResponseDto(Integer totalCnt, List<StoreResponseDto> storeCategoryList) {
        this.totalCnt = totalCnt;
        this.storeCategoryList = storeCategoryList;
    }
}
