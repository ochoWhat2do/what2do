package com.ocho.what2do.store.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreCategoryListResponseDto {
    private Integer totalCnt;       // 검색 시 api 내의 모든 결과 값 갯수
    private Boolean pageEnd;        // 마지막 페이지인지 아닌지 확인 (true : 마지막 페이지)
    private final List<StoreResponseDto> storeCategoryList;

    public StoreCategoryListResponseDto(Integer totalCnt, Boolean pageEnd, List<StoreResponseDto> storeCategoryList) {
        this.totalCnt = totalCnt;
        this.pageEnd = pageEnd;
        this.storeCategoryList = storeCategoryList;
    }
}
