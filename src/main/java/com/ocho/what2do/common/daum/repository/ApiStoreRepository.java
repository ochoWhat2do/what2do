package com.ocho.what2do.common.daum.repository;

import com.ocho.what2do.common.daum.entity.ApiStore;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiStoreRepository extends JpaRepository<ApiStore, Long> {
    @Cacheable("store_all")
    boolean existsApiStoreByStoreKey(String storeKey);

    Optional<ApiStore> findByStoreKey(String storeKey);

    Page<ApiStore> findByCategoryContains(String category, Pageable pageable);

    Page<ApiStore> findAll(Pageable pageable);

    List<ApiStore> findAllByCategoryContains(String category);
}