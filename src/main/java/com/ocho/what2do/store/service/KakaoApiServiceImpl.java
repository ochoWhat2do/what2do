package com.ocho.what2do.store.service;

import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.store.entity.ApiStore;
import com.ocho.what2do.store.repository.ApiStoreRepository;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
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
public class KakaoApiServiceImpl implements KakaoApiService {

  private final ApiStoreRepository apiStoreRepository;

  @Override
  public StoreListResponseDto fromJSONtoItems(String responseEntity) {
    JSONObject jsonObject = new JSONObject(responseEntity);
    JSONObject meta = jsonObject.getJSONObject("meta");

    Integer totalCnt = meta.getInt("total_count");
    Integer pageCnt = meta.getInt("pageable_count");

    JSONObject name = meta.getJSONObject("same_name");
    String keyWord = name.getString("keyword");
    String region = name.getString("selected_region");

    if (region.isBlank() || keyWord.isBlank()) {
      throw new CustomException(CustomErrorCode.NOT_FOUND_KEYWORD_REGION);
    }

    JSONArray documents = jsonObject.getJSONArray("documents");
    List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();

    for (Object item : documents) {
      StoreResponseDto storeResponseDto = new StoreResponseDto((JSONObject) item);
      Optional<ApiStore> apiStore = apiStoreRepository.findByStoreKey(
          storeResponseDto.getStoreKey());
      List<S3FileDto> fileDtoList = null;
      if (apiStore.isPresent()) {
        if (apiStore.get().getImages() != null) {
          fileDtoList = apiStore.get().getImages();
          storeResponseDto.setImages(fileDtoList);
        }
      }

      ApiStore store = ApiStore.builder().storeKey(storeResponseDto.getStoreKey())
          .title(storeResponseDto.getTitle())
          .homePageLink(storeResponseDto.getHomePageLink())
          .category(storeResponseDto.getCategory())
          .address(storeResponseDto.getAddress())
          .roadAddress(storeResponseDto.getRoadAddress())
          .latitude(storeResponseDto.getLatitude())
          .longitude(storeResponseDto.getLongitude())
          .images(fileDtoList)
          .build();

      storeResponseDtoList.add(storeResponseDto);
      try {
        if (!apiStoreRepository.existsApiStoreByStoreKey(store.getStoreKey())) {
          apiStoreRepository.save(store);
        }
      } catch (DataIntegrityViolationException e) {
        continue;
      }
    }
    return new StoreListResponseDto(totalCnt, pageCnt, keyWord, region, storeResponseDtoList);
  }
}