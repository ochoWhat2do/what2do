package com.ocho.what2do.common.daum.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Getter
@NoArgsConstructor
public class DaumDto {
    private String placeName;
    private String placeUrl;
    private String categoryName;
    private String addressName;
    private String roadAddressName;
    private String x;
    private String y;

    public DaumDto(JSONObject itemJson) {
        this.placeName = itemJson.getString("place_name");
        this.placeUrl = itemJson.getString("place_url");
        this.categoryName = itemJson.getString("category_name");
        this.addressName = itemJson.getString("address_name");
        this.roadAddressName = itemJson.getString("road_address_name");
        this.x = itemJson.getString("x");
        this.y = itemJson.getString("y");
    }
}
