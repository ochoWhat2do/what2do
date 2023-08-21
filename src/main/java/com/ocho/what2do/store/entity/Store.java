package com.ocho.what2do.store.entity;

import com.ocho.what2do.comment.entity.CommentLike;
import com.ocho.what2do.common.entity.Timestamped;
import com.ocho.what2do.storecategory.entity.StoreCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "title", length = 500, nullable = false)
  private String title;                   // 음식명, 장소명
  @Column(name = "address", length = 500)
  private String address;                 // 주소
  @Column(name = "read_address", length = 500)
  private String readAddress;             // 도로명
  @Column(name = "homepage_link", length = 500)
  private String homePageLink;            // 홈페이지 주소
  @Column(name = "image_link", length = 500)
  private String imageLink;               // 음식, 가게 이미지 주소
  @Column(columnDefinition = "tinyint(1)", name = "is_visit")
  private boolean isVisit;                // 방문 여부
  @Column(name = "visit_count")
  private int visitCount;                 // 방문 횟수
  @Column(name = "last_visit_date")
  private LocalDateTime lastVisitDate;    // 마지막 방문 일자

  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<StoreCategory> storeCategoryList = new ArrayList<>();
}