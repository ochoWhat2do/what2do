package com.ocho.what2do.store.entity;

import com.ocho.what2do.common.file.S3FileDto;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "store_key", length = 500, unique = true)
    private String storeKey;
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
    @Type(JsonType.class)
    @Column(name = "images", columnDefinition = "json")
    private List<S3FileDto> images;

    @Builder
    public ApiStore(String storeKey, String title, String homePageLink, String category, String address, String roadAddress, String latitude, String longitude, List<S3FileDto> images) {
        this.storeKey = storeKey;
        this.title = title;
        this.homePageLink = homePageLink;
        this.category = category;
        this.address = address;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
    }

    public void update(List<S3FileDto> images) {
        this.images = images;
    }
}
