package com.ocho.what2do.store.service;

import com.ocho.what2do.store.entity.ApiStore;
import com.ocho.what2do.store.dto.StoreCategoryListResponseDto;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteListResponseDto;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteResponseDto;
import com.ocho.what2do.user.entity.User;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

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
     * @param page 페이징
     * @return api_store 테이블의 모든 가게
     */
    StoreListResponseDto getStores(int page);

    /**
     * 가게 단건 조회
     * @param storeKey storeKey 를 변수로 해당되는 가게 단건 조회
     * @param user user 정보
     * @return StoreResponseDto
     */
    StoreResponseDto getStore(String storeKey, User user);

    /**
     * 가게 카테고리별 조회
     * @param category 조회할 가게 category
     * @param page 페이징
     * @return category 가 포함되는 모든 정보
     */
    StoreCategoryListResponseDto getStoreCategory(String category, int page);

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
    ApiStore findByStoreKey(String storeKey);

    /**
     * 페이징 기능 추가 시 1 페이지부터 시작 하도록 설정
     * @param page 페이지의 int 값
     * @return int -> PageRequest 값
     */
    PageRequest pageable(int page);

    /**
     * 페이지 수 체크 및 반환
     * @param totalCnt 검색한 결과 값들의 갯수
     * @return 페이지 최대 수 반환
     */
    public int pageCnt(int totalCnt);

    /**
     * 주소로 가게 조회
     * @param address 조회할 가게의 주소
     * @param user 사용자 조회
     * @return 해당 주소에 속한 가게
     */
    StoreResponseDto getStoresByAddress(String address, User user);

    /**
     * 리뷰 건수를 기준으로 가게와 리뷰를 조인하여 조회하고, 리뷰 건수 순으로 정렬합니다.
     * @param page    페이지 번호
     * @param size    페이지 크기
     * @param sortBy  정렬 기준 필드 (예: "title" 또는 "createdAt")
     * @param isAsc   오름차순 정렬 여부 (true: 오름차순, false: 내림차순)
     * @return 리뷰 건수가 높은 순으로 정렬된 가게 목록
     */
    List<StoreResponseDto> findStoresListReview(int page, int size, String sortBy, boolean isAsc);


    /**
     * 첨부파일 정보업데이트
     * @param storeKeyList    스토어 키 목록
     * @param files    파일정보
     * @return int 성공여부
     */
    int updateApiStore(List<String> storeKeyList, List<MultipartFile> files);

    /**
     * 스케줄링 시 가게 Entity 단건 조회 (없는지 체크하지 않을 것 )
     * @param storeKey 조회할 가게 storeKey
     * @return 가게 API Entity
     */
    ApiStore getApiStoreNotCheck(String storeKey);
}
