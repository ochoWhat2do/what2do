package com.ocho.what2do.admin.service;

import com.ocho.what2do.admin.dto.*;
import com.ocho.what2do.store.entity.ApiStore;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminStoreService {

  /**
   * 가게 생성
   *
   * @param requestDto 가게 생성 요청 정보
   * @param user       가게 생성 요청자
   * @param files      가게 첨부 이미지
   * @return 가게 생성 결과
   */
  AdminStoreResponseDto createStore(AdminStoreRequestDto requestDto, User user, List<MultipartFile> files);

  /**
   * 전체 가게 목록 조회
   * @return 전체 가게 목록
   */
  AdminApiStoreListResponseDto getStores(String keyword, int page, int size, String sortBy, boolean isAsc);

  /**
   * 가게 단건 조회
   * @param storeId 조회할 가게 ID
   * @return 조회된 가게 정보
   */
  AdminStoreViewResponseDto getStoreById(Long storeId, User user);

  /**
   * API 가게 단건 조회
   * @param storeKey 조회할 가게 StoreKey
   * @return 조회된 가게 정보
   */
  AdminApiStoreViewResponseDto getApiStoreByStoreKey(String storeKey, User user);

  /**
   * API 가게 업데이트
   *
   * @param storeKey    업데이트 할 가게
   * @param requestDto 업데이트 할 가게 정보
   * @param user       가게 업데이트 요청자
   * @param files      업데이트할 가게 첨부 사진
   * @return 업데이트된 가게 정보
   */
  AdminApiStoreResponseDto updateApiStore(String storeKey, AdminApiStoreRequestDto requestDto, User user, List<MultipartFile> files);

  /**
   * 가게 업데이트
   *
   * @param storeId    업데이트 할 가게
   * @param requestDto 업데이트 할 가게 정보
   * @param user       가게 업데이트 요청자
   * @param files      업데이트할 가게 첨부 사진
   * @return 업데이트된 가게 정보
   */
  AdminStoreResponseDto updateStore(Long storeId, AdminStoreRequestDto requestDto, User user, List<MultipartFile> files);

  /**
   * 가게 삭제
   * @param storeId 삭제 요청 가게
   * @param user 가게 삭제 요청자
   */
  void deleteStore(Long storeId, User user);

  /**
   * API 가게 Entity 단건 조회
   * @param storeKey 조회할 가게 ID
   * @return 가게 Entity
   */
  ApiStore findApiStore(String storeKey);

  /**
   * 가게 Entity 단건 조회
   * @param storeId 조회할 가게 ID
   * @return 가게 Entity
   */
  Store findStore(Long storeId);

  /**
   * 사용자 찾기
   * @param user 사용자 찾기
   * @return 가게 Entity
   */
  User findUser(User user);

}
