package com.ocho.what2do.storefavorite.dto;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import com.ocho.what2do.user.entity.User;

public class StoreFavoriteResponseDto extends ApiResponseDto {
    private Long id;
    private User user;
    private Store store;

    public StoreFavoriteResponseDto(StoreFavorite storeFavorite){
        this.id = storeFavorite.getId();
        this.user = storeFavorite.getUser();
        this.store = storeFavorite.getStore();
    }
}
