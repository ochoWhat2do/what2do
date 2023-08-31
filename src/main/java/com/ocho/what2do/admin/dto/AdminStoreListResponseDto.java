package com.ocho.what2do.admin.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class AdminStoreListResponseDto {
    private List<AdminStoreResponseDto> storesList;

    public AdminStoreListResponseDto(List<AdminStoreResponseDto> storeList) {
        this.storesList = storeList;
    }
}
