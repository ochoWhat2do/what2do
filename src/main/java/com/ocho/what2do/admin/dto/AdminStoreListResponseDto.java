package com.ocho.what2do.admin.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AdminStoreListResponseDto {
    private List<AdminStoreResponseDto> storesList;

    public AdminStoreListResponseDto(List<AdminStoreResponseDto> storeList) {
        this.storesList = storeList;
    }
}
