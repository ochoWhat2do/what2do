package com.ocho.what2do.store.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreListResponseDto {
    private List<StoreResponseDto> storesList;

    public StoreListResponseDto(List<StoreResponseDto> storeList) {
        this.storesList = storeList;
    }
}
