package com.ocho.what2do.store.service;

import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreRequestDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteListResponseDto;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteResponseDto;
import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import com.ocho.what2do.storefavorite.repository.StoreFavoriteRepository;
import com.ocho.what2do.user.entity.User;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final StoreFavoriteRepository storeUserRepository;

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
    @Transactional
    public StoreResponseDto updateStore(Store store, StoreRequestDto requestDto, User user) {
        store.update(requestDto);
        return new StoreResponseDto(store);
    }

    @Override
    @Transactional
    public void deleteStore(Store store, User user) {
        storeRepository.delete(store);
    }


    @Override
    public StoreFavoriteResponseDto addStoreFavorite(Long storeId, User user) {
        Store store = findStore(storeId);
        if(storeUserRepository.existsByUserAndStore(user,store)){
            throw new DuplicateRequestException("이미 찜한 가게입니다.");
        }else {
            StoreFavorite storeUser = new StoreFavorite(store, user);
            storeUserRepository.save(storeUser);
        }
        return new StoreFavoriteResponseDto(new StoreFavorite(store,user));
    }

    @Override
    public StoreFavoriteListResponseDto getStoreFavorite(User user) {
        List<StoreFavoriteResponseDto> storeUserList = storeUserRepository.findAll().stream().map(StoreFavoriteResponseDto::new).collect(Collectors.toList());
        return new StoreFavoriteListResponseDto(storeUserList);
    }

    @Override
    @Transactional
    public void deleteStoreFavorite(Long storeId, User user) {
        Store store = findStore(storeId);
        Optional<StoreFavorite> storeUserOptional = storeUserRepository.findByUserAndStore(user, store);
        if(storeUserOptional.isPresent()){
            storeUserRepository.delete(storeUserOptional.get());
        }else {
            throw new IllegalArgumentException("해당 가게에 취소할 찜이 없습니다.");
        }
    }
    @Override
    public Store findStore(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(()->new CustomException(CustomErrorCode.STORE_NOT_FOUND,null));
    }
}
