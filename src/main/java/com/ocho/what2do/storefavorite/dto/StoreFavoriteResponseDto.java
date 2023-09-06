package com.ocho.what2do.storefavorite.dto;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import com.ocho.what2do.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreFavoriteResponseDto extends ApiResponseDto {
    private Long id;
    private User user;
    private String title;
    private String email;
    private String storeKey;
    private String category;
    private String address;
    private String homePageLink;

    public StoreFavoriteResponseDto(StoreFavorite storeFavorite){
        this.id = storeFavorite.getId();
        this.email = storeFavorite.getUser().getEmail();
        this.title = storeFavorite.getStore().getTitle();
        this.storeKey = storeFavorite.getStore().getStoreKey();
        this.category = storeFavorite.getStore().getCategory();
        this.address = storeFavorite.getStore().getAddress();
        this.homePageLink =storeFavorite.getStore().getHomePageLink();
    }
}
