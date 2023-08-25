package com.ocho.what2do.common.naver.service;

import com.ocho.what2do.common.naver.dto.NaverDto;

import java.util.List;

public interface NaverApiService {

    /**
     * NAVER 검색 API 사용
     * @param query 조회할 키워드
     * @return 키워드로 조회된 목록
     */
    List<NaverDto> searchItems(String query);

    /**
     * JSON 처리를 도와주는 라이브러리를 추가하여 받아온 JSON 형태의 String 을 처리
     * @param responseEntity NAVER 검색 API 로 받아온 JSON 형태의 값을 String 으로 받아옴 (중첩 JSON)
     * @return 받아온 값을 Dto 로 변환하여 List 로 반환
     */
    List<NaverDto> fromJSONtoItems(String responseEntity);
}