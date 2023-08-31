package com.ocho.what2do.store.service;

import com.ocho.what2do.common.daum.entity.ApiStore;
import com.ocho.what2do.common.daum.repository.ApiStoreRepository;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.Store;
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
    private final ApiStoreRepository apiStoreRepository;
    private final StoreFavoriteRepository storeFavoriteRepository;

    @Override
    @Transactional(readOnly = true)
    public StoreListResponseDto getStores() {
        List<StoreResponseDto> storeList = apiStoreRepository.findAll().stream().map(StoreResponseDto::new).collect(Collectors.toList());
        return new StoreListResponseDto(storeList);
    }

    @Override
    @Transactional
    public StoreResponseDto getStore(String storeKey) {
        ApiStore findStore = findStoreKey(storeKey);
        Store store = Store.builder().storeKey(findStore.getStoreKey())
                .title(findStore.getTitle())
                .homePageLink(findStore.getHomePageLink())
                .category(findStore.getCategory())
                .address(findStore.getAddress())
                .roadAddress(findStore.getRoadAddress())
                .latitude(findStore.getLatitude())
                .longitude(findStore.getLongitude())
                .build();
        if (!storeRepository.existsStoreByStoreKey(store.getStoreKey())) {
            storeRepository.save(store);
        }
        return new StoreResponseDto(findStore);
    }

    @Override
    @Transactional(readOnly = true)
    public StoreFavoriteListResponseDto getStoreFavorite(User user) {
        List<StoreFavoriteResponseDto> storeUserList = storeFavoriteRepository.findAll().stream().map(StoreFavoriteResponseDto::new).collect(Collectors.toList());
        return new StoreFavoriteListResponseDto(storeUserList);
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
        return storeRepository.findById(storeId).orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND));
    }

    @Override
    public ApiStore findStoreKey(String storeKey) {
        return apiStoreRepository.findByStoreKey(storeKey).orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND));
    }
}