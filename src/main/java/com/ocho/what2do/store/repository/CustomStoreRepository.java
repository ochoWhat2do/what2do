package com.ocho.what2do.store.repository;

import com.ocho.what2do.store.dto.StoreResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomStoreRepository {
    List<StoreResponseDto> findStoresListReview(Pageable pageable);
}
