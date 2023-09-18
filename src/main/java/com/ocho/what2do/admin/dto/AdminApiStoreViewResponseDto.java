package com.ocho.what2do.admin.dto;

import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.store.entity.ApiStore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminApiStoreViewResponseDto {
  private Long id;
  private String title;
  private String homePageLink;
  private String category;
  private String address;
  private String roadAddress;
  private List<S3FileDto> images;

  public AdminApiStoreViewResponseDto(ApiStore store) {
    this.id = store.getId();
    this.title = store.getTitle();
    this.homePageLink = store.getHomePageLink();
    this.category = store.getCategory();
    this.address = store.getAddress();
    this.roadAddress = store.getRoadAddress();
    this.images = store.getImages();
  }
}
