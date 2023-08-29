package com.ocho.what2do.common.daum.service;

import com.ocho.what2do.store.dto.StoreApiDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.repository.StoreRepository;
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

@Slf4j(topic = "DAUM API")
@Service
@RequiredArgsConstructor
public class DaumApiServiceImpl implements DaumApiService {

    private final RestTemplate restTemplate;
    private final StoreRepository storeRepository;

    @Value("${kakao.Authorization}")
    private String Authorization;

    @Override
    public List<StoreApiDto> searchItems(String query, String page, String size) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", query) // 검색어
                .queryParam("page", page) // 페이징
                .queryParam("size", size) // 한 페이지에 출력 할 값의 갯수
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

        return fromJSONtoItems(responseEntity.getBody());
    }

    @Override
    public List<StoreApiDto> fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray documents = jsonObject.getJSONArray("documents");
        List<StoreApiDto> storeApiDtoList = new ArrayList<>();

        for (Object item : documents) {
            StoreApiDto storeApiDto = new StoreApiDto((JSONObject) item);
            Store store = Store.builder().title(storeApiDto.getTitle())
                    .homePageLink(storeApiDto.getHomePageLink())
                    .category(storeApiDto.getCategory())
                    .address(storeApiDto.getAddress())
                    .roadAddress(storeApiDto.getRoadAddress())
                    .latitude(storeApiDto.getLatitude())
                    .longitude(storeApiDto.getLongitude())
                    .build();
            if (!storeRepository.existsStoreByAddress(store.getAddress())) {
                storeRepository.save(store);
            }
            storeApiDtoList.add(storeApiDto);
        }

        return storeApiDtoList;
    }
}