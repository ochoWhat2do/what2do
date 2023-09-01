package com.ocho.what2do.store.service;

import com.ocho.what2do.common.daum.entity.ApiStore;
import com.ocho.what2do.store.dto.StoreCategoryListResponseDto;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteListResponseDto;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteResponseDto;
import com.ocho.what2do.user.entity.User;

public interface StoreService {

    /**
     * 가게 찜 목록 조회
     * @param user 가게 찜 목록을 가지고 있는 사용자 ID??
     * @return 조회된 가게 찜 목록
     */
    StoreFavoriteListResponseDto getStoreFavorite(User user);

    /**
     * 가게 찜하기
     * @param storeId 찜하기 요청 가게 ID
     * @param user    찜하기 요청 사용자
     * @return 찜된 가게 정보
     */
    StoreFavoriteResponseDto addStoreFavorite(Long storeId, User user);

    /**
     * 가게 찜하기 취소
     * @param storeId 찜하기 취소 요청 가게 ID
     * @param user    찜하기 취소 요청 사용자
     */
    void deleteStoreFavorite(Long storeId, User user);

    /**
     * 가게 전체 조회
     * @return api_store 테이블의 모든 가게
     */
    StoreListResponseDto getStores();

    /**
     * 가게 단건 조회
     * @param storeKey storeKey 를 변수로 해당되는 가게 단건 조회
     * @return StoreResponseDto
     */
    StoreResponseDto getStore(String storeKey);

    /**
     * 가게 Entity 단건 조회
     * @param storeId 조회할 가게 storeId
     * @return 가게 Entity
     */
    Store findStore(Long storeId);

    /**
     * 가게 Entity 단건 조회
     * @param storeKey 조회할 가게 storeKey
     * @return 가게 API Entity
     */
    ApiStore findStoreKey(String storeKey);

    /**
     * 가게 카테고리별 조회
     * @param category 조회할 가게 category
     * @return category 가 포함되는 모든 정보
     */
    StoreCategoryListResponseDto getStoreCategory(String category);
}
