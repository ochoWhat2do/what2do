package com.ocho.what2do.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminStoreRequestDto {
    private String title;
    private String homePageLink;
    private String category;
    private String address;
    private String roadAddress;
    private String latitude;
    private String longitude;
    private String storeKey;

    @Builder
    public AdminStoreRequestDto(String title, String homePageLink, String category, String address, String roadAddress, String latitude, String longitude, String storeKey) {
        this.title = title;
        this.homePageLink = homePageLink;
        this.category = category;
        this.address = address;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeKey = storeKey;
    }
}
