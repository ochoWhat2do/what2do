package com.ocho.what2do.store.dto;

import java.time.LocalDateTime;

public class StoreApiDto {

  private int index;
  private String title;                   // 음식명, 장소명
  private String category;                // 카테고리
  private String address;                 // 주소
  private String readAddress;             // 도로명
  private String homePageLink;            // 홈페이지 주소
  private String imageLink;               // 음식, 가게 이미지 주소
  private boolean isVisit;                // 방문 여부
  private int visitCount;                 // 방문 횟수
  private LocalDateTime lastVisitDate;    // 마지막 방문 일자
}