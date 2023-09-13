package com.ocho.what2do.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ocho.what2do.common.daum.entity.ApiStore;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import com.ocho.what2do.user.entity.User;
import lombok.Getter;
import org.json.JSONObject;

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
        Optional<StoreFavorite> userFavorite = store.getStoreFavoriteList().stream().filter(v -> v.getUser().getId().equals(loginUser.getId())).findFirst();
        if (userFavorite.isPresent()) {
            this.favoriteYn = true;
        } else {
            this.favoriteYn = false;
        }
    }

}
