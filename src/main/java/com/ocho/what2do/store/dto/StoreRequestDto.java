package com.ocho.what2do.store.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreRequestDto {
    private String title;
    private String address;
    private String readAddress;
    private String homePageLink;
    private String imageLink;
    private boolean isVisit;
    private int visitCount;

    @Builder
    public StoreRequestDto(String title, String address, String readAddress, String homePageLink, String imageLink, boolean isVisit, int visitCount){
        this.title = title;
        this.address = address;
        this.readAddress = readAddress;
        this.homePageLink = homePageLink;
        this.imageLink = imageLink;
        this.isVisit = isVisit;
        this.visitCount = visitCount;
    }
}
