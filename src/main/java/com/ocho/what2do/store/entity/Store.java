package com.ocho.what2do.store.entity;

import com.ocho.what2do.store.dto.StoreRequestDto;
import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "title", length = 500, nullable = false)
  private String title;                   // 가게명
  @Column(name = "homepage_link", length = 500)
  private String homePageLink;            // 홈페이지 주소
  @Column(name = "category", length = 500)
  private String category;
  @Column(name = "address", length = 500)
  private String address;                 // 주소
  @Column(name = "road_address", length = 500)
  private String roadAddress;             // 도로명
  @Column(name = "latitude", length = 500)
  private String latitude;                // 가게 x 좌표
  @Column(name = "longitude", length = 500)
  private String longitude;               // 가게 y 좌표

//  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
//  private List<StoreCategory> storeCategoryList = new ArrayList<>();

  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<StoreFavorite> storeFavoriteList = new ArrayList<>();

  @Builder
  public Store(String title, String homePageLink, String category, String address, String roadAddress, String latitude, String longitude){
    this.title = title;
    this.homePageLink = homePageLink;
    this.category = category;
    this.address = address;
    this.roadAddress = roadAddress;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public void update(StoreRequestDto requestDto){
    this.title = requestDto.getTitle();
    this.homePageLink = requestDto.getHomePageLink();
    this.category = requestDto.getCategory();
    this.address = requestDto.getAddress();
    this.roadAddress = requestDto.getRoadAddress();
    this.latitude = requestDto.getLatitude();
    this.longitude = requestDto.getLongitude();
  }
}
