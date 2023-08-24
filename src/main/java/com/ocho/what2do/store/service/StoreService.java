package com.ocho.what2do.store.service;

import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreRequestDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteListResponseDto;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteResponseDto;
import com.ocho.what2do.user.entity.User;

public interface StoreService{

        /**
         * 가게 생성
         * @param requestDto 가게 생성 요청 정보
         * @param user 가게 생성 요청자
         * @return 가게 생성 결과
         */
        StoreResponseDto createStore(StoreRequestDto requestDto, User user);

        /**
         * 전체 가게 목록 조회
         * @return 전체 가게 목록
         */
        StoreListResponseDto getStores();

        /**
         * 가게 단건 조회
         * @param storeId 조회할 가게 ID
         * @return 조회된 가게 정보
         */
        StoreResponseDto getStoreById(Long storeId);

        /**
         * 가게 업데이트
         * @param store 업데이트 할 가게
         * @param requestDto 업데이트 할 가게 정보
         * @param user 가게 업데이트 요청자
         * @return 업데이트된 가게 정보
         */
        StoreResponseDto updateStore(Store store, StoreRequestDto requestDto, User user);

        /**
         * 가게 삭제
         * @param store 삭제 요청 가게
         * @param user 가게 삭제 요청자
         */
        void deleteStore(Store store, User user);

        /**
         * 가게 Entity 단건 조회
         * @param storeId 조회할 가게 ID
         * @return 가게 Entity
         */
        Store findStore(Long storeId);

        /**
         * 가게 찜하기
         * @param storeId 찜하기 요청 가게 ID
         * @param user    찜하기 요청 사용자
         * @return 찜된 가게 정보
         */
        StoreFavoriteResponseDto addStoreFavorite(Long storeId, User user);

        /**
         *  가게 찜 목록 조회
         * @param user 가게 찜 목록을 가지고 있는 사용자 ID??
         * @return 조회된 가게 찜 목록
         */
        StoreFavoriteListResponseDto getStoreFavorite(User user);

        /**
         * 가게 찜하기 취소
         * @param storeId 찜하기 취소 요청 가게 ID
         * @param user    찜하기 취소 요청 사용자
         */
        void deleteStoreFavorite(Long storeId, User user);
}
