package com.ocho.what2do.storefavorite.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreFavoriteListResponseDto {
    private List<StoreFavoriteResponseDto> storeFavoriteList;

    public StoreFavoriteListResponseDto(List<StoreFavoriteResponseDto> storeFavoriteList) {
        this.storeFavoriteList = storeFavoriteList;
    }
}
