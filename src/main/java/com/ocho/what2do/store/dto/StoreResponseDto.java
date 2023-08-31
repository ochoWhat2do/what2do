package com.ocho.what2do.store.dto;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreResponseDto {
    private Long id;
    private String storeKey;
    private String title;
    private String homePageLink;
    private String category;
    private String address;
    private String roadAddress;
    private String latitude;
    private String longitude;
//    private List<StoreCategory> storeCategoryList;

    private boolean isStoreFavorite = false;

    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.storeKey = store.getStoreKey();
        this.title = store.getTitle();
        this.homePageLink = store.getHomePageLink();
        this.category = store.getCategory();
        this.address = store.getAddress();
        this.roadAddress = store.getRoadAddress();
        this.latitude = store.getLatitude();
        this.longitude = store.getLongitude();
//        this.storeCategoryList = store.getStoreCategoryList().stream().toList();
        this.isStoreFavorite = false;
    }
}
