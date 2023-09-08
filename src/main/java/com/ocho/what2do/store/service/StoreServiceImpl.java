package com.ocho.what2do.store.service;

import com.ocho.what2do.common.daum.entity.ApiStore;
import com.ocho.what2do.common.daum.repository.ApiStoreRepository;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.dto.StoreCategoryListResponseDto;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final int pageSize = 20;

    @Override
    @Transactional(readOnly = true)
    @Cacheable("store_all")
    public StoreListResponseDto getStores(int page) {
        PageRequest pageRequest = pageable(page);
        List<StoreResponseDto> storeList = apiStoreRepository.findAll(pageRequest).stream().map(StoreResponseDto::new).collect(Collectors.toList());
        Integer totalCnt = apiStoreRepository.findAll().size();
        Boolean pageEnd = pageEnd(totalCnt, page);

        if (pageCnt(totalCnt) < page) {
            throw new CustomException(CustomErrorCode.NOT_FOUND_PAGE);
        }

        return new StoreListResponseDto(totalCnt, pageEnd, storeList);
    }

    @Override
    @Transactional
    @Cacheable(value = "store_one", key = "#storeKey")
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
        Optional<Store> savedStore = null;
        // 프론트에서는 db에 저장된 정보를 활용할 필요가 있다. storeKey 로 조회할 때는 ApiStore 테이블에서 조회, storeId가 필요할때는 Store 테이블에서 조회
        if (!storeRepository.existsStoreByStoreKey(store.getStoreKey())) {
            savedStore = Optional.of(storeRepository.save(store));
        } else {
            savedStore = storeRepository.getStoreByStoreKey(storeKey);
        }
        if (savedStore.isPresent()) {
            return new StoreResponseDto(savedStore.get());
        } else {
            return new StoreResponseDto(findStore);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("store_all")
    public StoreCategoryListResponseDto getStoreCategory(String category, int page) {
        PageRequest pageRequest = pageable(page);
        List<StoreResponseDto> storeCategory = apiStoreRepository.findByCategoryContains(category, pageRequest).stream().map(StoreResponseDto::new).toList();
        Integer totalCnt = apiStoreRepository.findAllByCategoryContains(category).stream().toList().size();
        Boolean pageEnd = pageEnd(totalCnt, page);

        if (pageCnt(totalCnt) < page) {
            throw new CustomException(CustomErrorCode.NOT_FOUND_PAGE);
        }

        return new StoreCategoryListResponseDto(totalCnt, pageEnd, storeCategory);
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

    @Override
    public PageRequest pageable(int page) {
        /*
         * pageRequest.of(page, size, sort)
         * page : page -1 -> PageConfig 에서 page 시작 값을 1로 잡았지만 내부적으로는 0부터 시작으로 -1 을 진행
         * size : 한 page 의 출력 갯수
         * Sort.by("") : 정렬 기준
         * ascending : 오름차순 (default)
         * descending : 내림차순
         */
        return PageRequest.of(page - 1, pageSize, Sort.by("id"));
    }

    @Override
    public Boolean pageEnd(int totalCnt, int page) {
        if (Math.floorDiv(totalCnt, pageSize) == page - 1) {
            return Math.floorMod(totalCnt, pageSize) != 0;
        } return false;
    }

    public int pageCnt(int totalCnt) {
        if (Math.floorMod(totalCnt, pageSize) == 0 ) {
            return Math.floorDiv(totalCnt, pageSize);
        } return Math.floorDiv(totalCnt, pageSize) + 1;
    }
}