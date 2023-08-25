package com.ocho.what2do.common.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Getter
@NoArgsConstructor
public class NaverDto {
    private String title;
    private String link;
    private String category;
    private String address;
    private String roadAddress;
    private int mapx;
    private int mapy;

    public NaverDto(JSONObject itemJson) {
        this.title = itemJson.getString("title");
        this.link = itemJson.getString("link");
        this.category = itemJson.getString("category");
        this.address = itemJson.getString("address");
        this.roadAddress = itemJson.getString("roadAddress");
        this.mapx = itemJson.getInt("mapx");
        this.mapy = itemJson.getInt("mapy");
    }
}
