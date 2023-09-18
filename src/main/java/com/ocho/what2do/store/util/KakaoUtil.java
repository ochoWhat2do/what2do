package com.ocho.what2do.store.util;

import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j(topic = "Kakao Local Api 호출")
@Component
@RequiredArgsConstructor
public class KakaoUtil {

  private final RestTemplate restTemplate;

  @Value("${kakao.Authorization}")
  private String Authorization;

  public static final String KAKAO_SEARCH_API = "https://dapi.kakao.com";
  public static final String KAKAO_SEARCH_PATH = "v2/local/search/keyword.json";

  private static final String QUERY = "query";
  private static final String PAGE = "page";
  private static final String SIZE = "size";
  private static final String AUTHORIZATION = "Authorization";

  //    @TimeTrace
  public String callApi(String query, String page) {
    // 요청 URL 만들기
    HttpHeaders headers = new HttpHeaders();
    headers.set(AUTHORIZATION, Authorization);

    // uri
    UriComponents uriComponents = UriComponentsBuilder.fromUriString(KAKAO_SEARCH_API)
        .path(KAKAO_SEARCH_PATH)
        .queryParam(QUERY, query)
        .queryParam(PAGE, page)
        .queryParam(SIZE, 15)
        .queryParam("category_group_code", "FD6") // FD6 음식점 코드 (음식점만 검색 결과로 나오게끔)
        .encode()
        .build();

    // request
    HttpEntity<?> entity = new HttpEntity<>(headers);

    HttpEntity<String> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.GET,
        entity,
        String.class);

    if (Integer.parseInt(page) > 3) {
      throw new CustomException(CustomErrorCode.DATA_NOT_FOUND);
    }
    return response.getBody();
  }
}
