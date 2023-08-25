package com.ocho.what2do.common.naver.service;

import com.ocho.what2do.common.naver.dto.NaverDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "NAVER API")
@Service
@RequiredArgsConstructor
public class NaverApiServiceImpl implements NaverApiService {

    private final RestTemplate restTemplate;

    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.secretId}")
    private String secretId;

    @Override
    public List<NaverDto> searchItems(String query) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("display", 15)
                .queryParam("query", query)
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", secretId)
                .build();

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        log.info("NAVER API Status Code : " + responseEntity.getStatusCode());

        return fromJSONtoItems(responseEntity.getBody());
    }

    @Override
    public List<NaverDto> fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray items = jsonObject.getJSONArray("items");
        List<NaverDto> naverDtoList = new ArrayList<>();

        for (Object item : items) {
            NaverDto naverDto = new NaverDto((JSONObject) item);
            naverDtoList.add(naverDto);
        }

        return naverDtoList;
    }
}