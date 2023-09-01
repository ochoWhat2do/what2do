package com.ocho.what2do.store.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreCategoryListResponseDto {
    private final List<StoreResponseDto> storeCategoryList;

    public StoreCategoryListResponseDto(List<StoreResponseDto> storeCategoryList) {
        this.storeCategoryList = storeCategoryList;
    }
}
