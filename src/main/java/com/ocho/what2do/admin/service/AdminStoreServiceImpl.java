package com.ocho.what2do.admin.service;

import com.ocho.what2do.admin.dto.AdminStoreListResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreRequestDto;
import com.ocho.what2do.admin.dto.AdminStoreResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreViewResponseDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminStoreServiceImpl implements AdminStoreService {

  private final StoreRepository storeRepository;

  @Override
  @Transactional
  public AdminStoreResponseDto createStore(AdminStoreRequestDto requestDto, User user) {
    Store store = Store.builder().title(requestDto.getTitle())
        .homePageLink(requestDto.getHomePageLink())
        .category(requestDto.getCategory())
        .address(requestDto.getAddress())
        .roadAddress(requestDto.getRoadAddress())
        .latitude(requestDto.getLatitude())
        .longitude(requestDto.getLongitude())
        .build();
    return new AdminStoreResponseDto(storeRepository.save(store));

  }

  @Override
  @Transactional
  public AdminStoreListResponseDto getStores() {
    List<AdminStoreResponseDto> storeList = storeRepository.findAll().stream()
        .map(AdminStoreResponseDto::new).collect(
            Collectors.toList());
    return new AdminStoreListResponseDto(storeList);
  }

  @Override
  @Transactional
  public AdminStoreViewResponseDto getStoreById(Long storeId, User user) {
    Store store = findStore(storeId);
    return new AdminStoreViewResponseDto(store, user);
  }

  @Override
  @Transactional
  public AdminStoreResponseDto updateStore(Long storeId, AdminStoreRequestDto requestDto,
      User user) {
    Store store = findStore(storeId);
    store.updateAdmin(requestDto);
    return new AdminStoreResponseDto(store);
  }

  @Override
  @Transactional
  public void deleteStore(Long storeId, User user) {
    storeRepository.deleteById(storeId);
  }

  @Override
  public Store findStore(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND, null));
  }
}
