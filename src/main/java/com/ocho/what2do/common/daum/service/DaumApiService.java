package com.ocho.what2do.common.daum.service;

import com.ocho.what2do.store.dto.StoreListResponseDto;

public interface DaumApiService {

    /**
     * DAUM 검색 API 사용
     * @param query 조회할 키워드
     * @param page  조회할 페이지
     * @return 키워드로 조회된 목록
     */
    StoreListResponseDto searchItems(String query, String page);

    /**
     * JSON 처리를 도와주는 라이브러리를 추가하여 받아온 JSON 형태의 String 을 처리
     * @param responseEntity DAUM 검색 API 로 받아온 JSON 형태의 값을 String 으로 받아옴 (중첩 JSON)
     * @return 받아온 값을 Dto 로 변환하여 List 로 반환
     */
    StoreListResponseDto fromJSONtoItems(String responseEntity);
}