package com.ocho.what2do.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.store.entity.ApiStore;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import com.ocho.what2do.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private int viewCount;
    private  boolean favoriteYn;

    private Long reviewCount; // reviewCount 필드 추가
    private List<S3FileDto> images;

    public List<S3FileDto> getImages() {
        return images;
    }

    public void setImages(List<S3FileDto> images) {
        this.images = images;
    }

    public StoreResponseDto(JSONObject itemJson) {
        this.storeKey = itemJson.getString("id");
        this.title = itemJson.getString("place_name");
        this.homePageLink = itemJson.getString("place_url");
        this.category = itemJson.getString("category_name");
        this.address = itemJson.getString("address_name");
        this.roadAddress = itemJson.getString("road_address_name");
        this.latitude = itemJson.getString("y");
        this.longitude = itemJson.getString("x");
    }

    public StoreResponseDto(ApiStore store) {
        this.id = store.getId();
        this.storeKey = store.getStoreKey();
        this.title = store.getTitle();
        this.homePageLink = store.getHomePageLink();
        this.category = store.getCategory();
        this.address = store.getAddress();
        this.roadAddress = store.getRoadAddress();
        this.latitude = store.getLatitude();
        this.longitude = store.getLongitude();
        this.images =store.getImages();
    }

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
        this.viewCount = store.getViewCount();
        this.images = store.getImages();
    }

    public StoreResponseDto(Store store, Long reviewCount) {
        this.id = store.getId();
        this.storeKey = store.getStoreKey();
        this.title = store.getTitle();
        this.homePageLink = store.getHomePageLink();
        this.category = store.getCategory();
        this.address = store.getAddress();
        this.roadAddress = store.getRoadAddress();
        this.latitude = store.getLatitude();
        this.longitude = store.getLongitude();
        this.viewCount = store.getViewCount();
        this.reviewCount = reviewCount;
        this.images = store.getImages();
    }

    @Builder
    public StoreResponseDto(Long id, String storeKey, String title, String homePageLink,
        String category, String address, String roadAddress, String latitude, String longitude,
        int viewCount, Long reviewCount, List<S3FileDto> images) {
        this.id = id;
        this.storeKey = storeKey;
        this.title = title;
        this.homePageLink = homePageLink;
        this.category = category;
        this.address = address;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.viewCount = viewCount;
        this.reviewCount = reviewCount;
        this.images = images;
    }

    public StoreResponseDto(Store store, User loginUser) {
        this.id = store.getId();
        this.storeKey = store.getStoreKey();
        this.title = store.getTitle();
        this.homePageLink = store.getHomePageLink();
        this.category = store.getCategory();
        this.address = store.getAddress();
        this.roadAddress = store.getRoadAddress();
        this.latitude = store.getLatitude();
        this.longitude = store.getLongitude();
        this.viewCount = store.getViewCount();
        this.images = store.getImages();
        Optional<StoreFavorite> userFavorite = store.getStoreFavoriteList().stream().filter(v -> v.getUser().getId().equals(loginUser.getId())).findFirst();
        if (userFavorite.isPresent()) {
            this.favoriteYn = true;
        } else {
            this.favoriteYn = false;
        }
    }

}
