package com.ocho.what2do.storefavorite.dto;

import java.util.List;

public class StoreFavoriteListResponseDto {
    private List<StoreFavoriteResponseDto> storeFavoriteList;

    public StoreFavoriteListResponseDto(List<StoreFavoriteResponseDto> storeFavoriteList) {
        this.storeFavoriteList = storeFavoriteList;
    }
}
