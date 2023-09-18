package com.ocho.what2do.store.service;

import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.ApiStore;
import com.ocho.what2do.store.repository.ApiStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j(topic = "DAUM API")
@Service
@RequiredArgsConstructor
public class KakaoApiServiceImpl implements KakaoApiService {

  private final ApiStoreRepository apiStoreRepository;

  @Override
  @Cacheable(cacheNames = "store_all", key = "#responseEntity")
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

      if (!apiStoreRepository.existsApiStoreByStoreKey(store.getStoreKey())) {
        apiStoreRepository.save(store);
      }
    }
    return new StoreListResponseDto(totalCnt, pageCnt, keyWord, region, storeResponseDtoList);
  }
}