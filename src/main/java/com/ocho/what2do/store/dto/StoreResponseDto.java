package com.ocho.what2do.store.dto;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.storecategory.entity.StoreCategory;

import java.time.LocalDateTime;
import java.util.List;

public class StoreResponseDto extends ApiResponseDto {
    private Long id;
    private String title;
    private String address;
    private String readAddress;
    private String homePageLink;
    private String imageLink;
    private boolean isVisit;
    private int visitCount;
    private LocalDateTime lastVisitDate;
    private List<StoreCategory> storeCategoryList;

    public StoreResponseDto(Store store){
        this.id = store.getId();
        this.title = store.getTitle();
        this.address = store.getAddress();
        this.readAddress = store.getReadAddress();
        this.homePageLink = store.getHomePageLink();
        this.imageLink = store.getImageLink();
        this.isVisit = store.isVisit();
        this.visitCount = store.getVisitCount();
        this.lastVisitDate = store.getLastVisitDate();
        this.storeCategoryList = store.getStoreCategoryList().stream().toList();
    }
}
