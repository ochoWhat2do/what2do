package com.ocho.what2do.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreListResponseDto {
    private Integer totalCnt;   // 검색 시 api 내의 모든 결과 값 갯수
    private Integer pageCnt;    // 모든 결과 값 중 볼 수 있는 자료 수 (최대 45)
    private String region;      // query(검색어)에서 지역 정보를 제외한 키워드
    private String keyWord;     // query(검색어)에서 현재 검색에 사용된 지역 정보
    private List<StoreResponseDto> storeList;   // 검색 가게 리스트

    public StoreListResponseDto(Integer totalCnt, Integer pageCnt, String keyWord, String region, List<StoreResponseDto> storeList) {
        this.totalCnt = totalCnt;
        this.pageCnt = pageCnt;
        this.keyWord = keyWord;
        this.region = region;
        this.storeList = storeList;
    }

    public StoreListResponseDto(Integer totalCnt, Integer pageCnt, List<StoreResponseDto> storeList) {
        this.totalCnt = totalCnt;
        this.pageCnt = pageCnt;
        this.storeList = storeList;
    }
}
