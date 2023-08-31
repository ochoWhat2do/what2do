package com.ocho.what2do.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreRequestDto {
    private String storeKey;
    private String title;
    private String homePageLink;
    private String category;
    private String address;
    private String roadAddress;
    private String latitude;
    private String longitude;
}
