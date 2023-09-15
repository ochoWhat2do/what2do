package com.ocho.what2do.store.util;

import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j(topic = "Kakao Local Api 호출")
@Component
@RequiredArgsConstructor
public class KakaoUtil {
    private final RestTemplate restTemplate;

    @Value("${kakao.Authorization}")
    private String Authorization;

    //    @TimeTrace
    public String callApi(String query, String page) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", query) // 검색어
                .queryParam("page", page) // 페이징
                .queryParam("size", "15") // 한 페이지에 출력 할 값의 갯수 (고정 값 설정)
                .queryParam("category_group_code", "FD6") // FD6 음식점 코드 (음식점만 검색 결과로 나오게끔)
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .header("Authorization", Authorization)
                .build();

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        log.info("DAUM API Status Code : " + responseEntity.getStatusCode());

        if (Integer.parseInt(page) > 3) {
            throw new CustomException(CustomErrorCode.DATA_NOT_FOUND);
        }
        return responseEntity.getBody();
    }
}
