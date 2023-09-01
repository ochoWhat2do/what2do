package com.ocho.what2do.admin.dto;

import com.ocho.what2do.common.file.S3FileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
    private List<S3FileDto> images;

    public List<S3FileDto> getImages() {
        return images;
    }
    @Builder
    public AdminStoreRequestDto(String title, String homePageLink, String category, String address, String roadAddress, String latitude, String longitude, String storeKey, List<S3FileDto> images) {
        this.title = title;
        this.homePageLink = homePageLink;
        this.category = category;
        this.address = address;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeKey = storeKey;
        this.images = images;
    }
}
