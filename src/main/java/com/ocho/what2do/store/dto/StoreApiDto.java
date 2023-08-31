package com.ocho.what2do.store.dto;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class StoreApiDto {

  private String storeKey;          // 가게 ID 값
  private String title;             // 가게명
  private String homePageLink;      // 가게 url
  private String category;          // 가게 카테고리
  private String address;           // 가게 주소
  private String roadAddress;       // 가게 도로명 주소
  private String latitude;          // 가게 x 좌표
  private String longitude;         // 가게 y

  public StoreApiDto(JSONObject itemJson) {
    this.storeKey = itemJson.getString("id");
    this.title = itemJson.getString("place_name");
    this.homePageLink = itemJson.getString("place_url");
    this.category = itemJson.getString("category_name");
    this.address = itemJson.getString("address_name");
    this.roadAddress = itemJson.getString("road_address_name");
    this.latitude = itemJson.getString("x");
    this.longitude = itemJson.getString("y");
  }
}
