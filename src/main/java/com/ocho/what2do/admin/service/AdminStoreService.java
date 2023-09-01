package com.ocho.what2do.admin.service;

import com.ocho.what2do.admin.dto.AdminStoreListResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreRequestDto;
import com.ocho.what2do.admin.dto.AdminStoreResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreViewResponseDto;
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
  AdminStoreListResponseDto getStores();

  /**
   * 가게 단건 조회
   * @param storeId 조회할 가게 ID
   * @return 조회된 가게 정보
   */
  AdminStoreViewResponseDto getStoreById(Long storeId, User user);

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
   * 가게 Entity 단건 조회
   * @param storeId 조회할 가게 ID
   * @return 가게 Entity
   */
  Store findStore(Long storeId);

}
