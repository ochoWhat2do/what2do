package com.ocho.what2do.common.daum.repository;

import com.ocho.what2do.common.daum.entity.ApiStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiStoreRepository extends JpaRepository<ApiStore, Long> {
    boolean existsApiStoreByAddress(String address);

    Optional<ApiStore> findByStoreKey(String storeKey);

    Page<ApiStore> findByCategoryContains(String category, Pageable pageable);

    Page<ApiStore> findAll(Pageable pageable);
}