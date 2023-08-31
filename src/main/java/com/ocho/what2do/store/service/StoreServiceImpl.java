package com.ocho.what2do.store.service;

import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreRequestDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.entity.StoreDetail;
import com.ocho.what2do.store.repository.StoreDetailRepository;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteListResponseDto;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteResponseDto;
import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import com.ocho.what2do.storefavorite.repository.StoreFavoriteRepository;
import com.ocho.what2do.user.entity.User;
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
    private final StoreDetailRepository storeDetailRepository;
    private final StoreFavoriteRepository storeFavoriteRepository;

    @Override
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto requestDto, User user) {
        Store store = Store.builder().storeKey(requestDto.getStoreKey())
                .title(requestDto.getTitle())
                .homePageLink(requestDto.getHomePageLink())
                .category(requestDto.getCategory())
                .address(requestDto.getAddress())
                .roadAddress(requestDto.getRoadAddress())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .build();
        return new StoreResponseDto(storeRepository.save(store));
    }

    @Override
    @Transactional(readOnly = true)
    public StoreListResponseDto getStores() {
        List<StoreResponseDto> storeList = storeRepository.findAll().stream().map(StoreResponseDto::new).collect(Collectors.toList());
        return new StoreListResponseDto(storeList);
    }

//    @Override
//    @Transactional
//    public StoreViewResponseDto getStoreById(Long storeId, User user) {
//        Store store = findStore(storeId);
//        return new StoreViewResponseDto(store, user);
//    }

    @Override
    @Transactional
    public StoreResponseDto getStoresKey(String storeKey) {
        Store findStore = findStoreKey(storeKey);
        StoreDetail store = StoreDetail.builder().storeKey(findStore.getStoreKey())
                .title(findStore.getTitle())
                .homePageLink(findStore.getHomePageLink())
                .category(findStore.getCategory())
                .address(findStore.getAddress())
                .roadAddress(findStore.getRoadAddress())
                .latitude(findStore.getLatitude())
                .longitude(findStore.getLongitude())
                .build();
        if (!storeDetailRepository.existsStoreDetailByStoreKey(store.getStoreKey())) {
            storeDetailRepository.save(store);
        }
        return new StoreResponseDto(findStore);
    }

    @Override
    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreRequestDto requestDto, User user) {
        Store store = findStore(storeId);
        store.update(requestDto);
        return new StoreResponseDto(store);
    }

    @Override
    @Transactional
    public void deleteStore(Long storeId, User user) {
        storeRepository.deleteById(storeId);
    }


    @Override
    @Transactional
    public StoreFavoriteResponseDto addStoreFavorite(Long storeId, User user) {
        Store store = findStore(storeId);
        if (storeFavoriteRepository.existsByUserAndStore(user, store)) {
            throw new CustomException(CustomErrorCode.STORE_FAVORITE_ALREADY_EXIST);
        }
        StoreFavorite storeUser = new StoreFavorite(store, user);
        storeFavoriteRepository.save(storeUser);

        return new StoreFavoriteResponseDto(storeUser);
    }

    @Override
    @Transactional
    public StoreFavoriteListResponseDto getStoreFavorite(User user) {
        List<StoreFavoriteResponseDto> storeUserList = storeFavoriteRepository.findAll().stream().map(StoreFavoriteResponseDto::new).collect(Collectors.toList());
        return new StoreFavoriteListResponseDto(storeUserList);
    }

    @Override
    @Transactional
    public void deleteStoreFavorite(Long storeId, User user) {
        Store store = findStore(storeId);
        Optional<StoreFavorite> storeUserOptional = storeFavoriteRepository.findByUserAndStore(user, store);
        if (storeUserOptional.isPresent()) {
            storeFavoriteRepository.delete(storeUserOptional.get());
        } else {
            throw new CustomException(CustomErrorCode.STORE_FAVORITE_NOT_FOUND, null);
        }
    }

    @Override
    public Store findStore(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND, null));
    }

    @Override
    public Store findStoreKey(String storeKey) {
        return storeRepository.findByStoreKey(storeKey).orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND, null));
    }
}
