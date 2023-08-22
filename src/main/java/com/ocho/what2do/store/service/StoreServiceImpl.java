package com.ocho.what2do.store.service;

import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreRequestDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    @Override
    public StoreResponseDto createStore(StoreRequestDto requestDto, User user) {
        Store store = Store.builder().title(requestDto.getTitle())
                .address(requestDto.getAddress())
                .readAddress(requestDto.getReadAddress())
                .homePageLink(requestDto.getHomePageLink())
                .imageLink(requestDto.getImageLink())
                .isVisit(requestDto.isVisit())
                .visitCount(requestDto.getVisitCount())
                .build();
        return new StoreResponseDto(storeRepository.save(store));

    }

    @Override
    public StoreListResponseDto getStores() {
        List<StoreResponseDto> storeList = storeRepository.findAll().stream().map(StoreResponseDto::new).collect(Collectors.toList());
        return new StoreListResponseDto(storeList);
    }

    @Override
    public StoreResponseDto getStoreById(Long storeId) {
        Store store = findStore(storeId);
        return new StoreResponseDto(store);
    }

    @Override
    public StoreResponseDto updateStore(Store store, StoreRequestDto requestDto, User user) {
        store.update(requestDto);
        return new StoreResponseDto(store);
    }

    @Override
    public void deleteStore(Store store, User user) {
        storeRepository.delete(store);
    }

    @Override
    public Store findStore(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(()->new IllegalArgumentException("선택한 가게는 존재하지 않습니다."));
    }
}
