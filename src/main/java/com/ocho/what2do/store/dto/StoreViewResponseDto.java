package com.ocho.what2do.store.dto;

import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.storecategory.entity.StoreCategory;
import com.ocho.what2do.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreViewResponseDto {

    private Long id;
    private String title;
    private String homePageLink;
    private String category;
    private String address;
    private String roadAddress;
    private List<StoreCategory> storeCategoryList;

    private boolean isStoreFavorite = false;

    public StoreViewResponseDto(Store store, User user) {
        this.id = store.getId();
        this.title = store.getTitle();
        this.homePageLink = store.getHomePageLink();
        this.category = store.getCategory();
        this.address = store.getAddress();
        this.roadAddress = store.getRoadAddress();
//        this.storeCategoryList = store.getStoreCategoryList().stream().toList();
        this.isStoreFavorite = (user == null ? false : store.getStoreFavoriteList().stream().filter(m -> m.getUser().getId() == user.getId()).toList().size() > 0 ? true : false);
    }
}
