package com.ocho.what2do.admin.dto;

import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.store.entity.ApiStore;
import com.ocho.what2do.store.entity.Store;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminApiStoreResponseDto {
  private Long id;
  private String title;
  private String homePageLink;
  private String category;
  private String address;
  private String roadAddress;
  private String latitude;
  private String longitude;
  private boolean isStoreFavorite = false;
  private List<S3FileDto> images;
  private String storeKey;

  public AdminApiStoreResponseDto(ApiStore store) {
    this.id = store.getId();
    this.title = store.getTitle();
    this.homePageLink = store.getHomePageLink();
    this.category = store.getCategory();
    this.address = store.getAddress();
    this.roadAddress = store.getRoadAddress();
    this.latitude = store.getLatitude();
    this.longitude = store.getLongitude();
    this.isStoreFavorite = false;
    this.images = store.getImages();
    this.storeKey = store.getStoreKey();
  }
}
