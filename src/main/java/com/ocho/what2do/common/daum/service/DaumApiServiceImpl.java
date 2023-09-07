package com.ocho.what2do.common.daum.service;

import com.ocho.what2do.common.daum.entity.ApiStore;
import com.ocho.what2do.common.daum.repository.ApiStoreRepository;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
    private final ApiStoreRepository apiStoreRepository;

    @Value("${kakao.Authorization}")
    private String Authorization;

    @Override
    public StoreListResponseDto searchItems(String query, String page) {
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
            throw new CustomException(CustomErrorCode.NOT_FOUND_PAGE);
        }
        return fromJSONtoItems(responseEntity.getBody());
    }

    @Override
    @Cacheable("store")
//    @Cacheable("store")
    public StoreListResponseDto fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray documents = jsonObject.getJSONArray("documents");
        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();

        for (Object item : documents) {
            StoreResponseDto storeResponseDto = new StoreResponseDto((JSONObject) item);
            ApiStore store = ApiStore.builder().storeKey(storeResponseDto.getStoreKey())
                    .title(storeResponseDto.getTitle())
                    .homePageLink(storeResponseDto.getHomePageLink())
                    .category(storeResponseDto.getCategory())
                    .address(storeResponseDto.getAddress())
                    .roadAddress(storeResponseDto.getRoadAddress())
                    .latitude(storeResponseDto.getLatitude())
                    .longitude(storeResponseDto.getLongitude())
                    .build();
            if (!apiStoreRepository.existsApiStoreByStoreKey(store.getStoreKey())) {
                apiStoreRepository.save(store);
            }
            storeResponseDtoList.add(storeResponseDto);
        }

        JSONObject meta = jsonObject.getJSONObject("meta");
        Integer totalCnt = meta.getInt("total_count");
        Integer pageCnt = meta.getInt("pageable_count");
        Boolean pageEnd = meta.getBoolean("is_end");

        JSONObject name = meta.getJSONObject("same_name");
        String keyWord = name.getString("keyword");
        String region = name.getString("selected_region");

        return new StoreListResponseDto(totalCnt, pageCnt, pageEnd, keyWord, region, storeResponseDtoList);
    }
}